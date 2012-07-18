package a;
import java.util.*;
import com.restfb.batch.*;
import com.restfb.*;
import com.restfb.json.*;
import com.restfb.types.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.net.URLEncoder;

public class SocialStrength {
  public final FacebookClient facebookClient;
  public static User u;
  public static boolean callSS(String accessToken){
	  SocialStrength ss=new SocialStrength(accessToken);
	  ss.calcSocialStrength();
	  ss.calcTopFriends();
	  System.out.print("writing.....");
	  try{ReaderWriter.writeProfile(u);return true;}catch(Exception e){e.printStackTrace();}
	  return false;
  }
  public static void main(String[] args){
  //  callSS("AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");      
  }

  SocialStrength(String accessToken) {
	  facebookClient = new DefaultFacebookClient(accessToken);
	  List<JsonObject>ob=facebookClient.executeQuery("select uid from user where uid=me()", JsonObject.class);
	  String id=ob.get(0).getString("uid");
	  try{u=ReaderWriter.readProfile(id);}catch(Exception e){u=new User();}
  }
 public void calcTopFriends(){
	 String frnds[]=(String[])u.f.keySet().toArray(new String[u.f.size()]);
	 double scr=0;Friend fr;
	 for(int i=0;i<frnds.length;i++,scr=0){
		 fr=u.f.get(frnds[i]);
		 if(Double.isNaN(fr.familyrels))fr.familyrels=0;
		 if(Double.isNaN(fr.geogp))fr.geogp=0;
		 if(Double.isNaN(fr.inrelwith))fr.inrelwith=0;
		 if(Double.isNaN(fr.phototags))fr.phototags=0;
		 if(Double.isNaN(fr.sharedfrnds))fr.sharedfrnds=0;
		 if(Double.isNaN(fr.sharedgrps))fr.sharedgrps=0;
		 if(Double.isNaN(fr.wallposts))fr.wallposts=0;
		 scr=fr.familyrels+fr.geogp+fr.inrelwith+fr.phototags+fr.sharedfrnds+fr.sharedgrps+fr.wallposts;
		 u.socialstrength.put(frnds[i],scr);
	 }
 }
public void calcSocialStrength(){
	  Friend fr=null;
	  JsonArray friends=null;
	  JsonObject user=facebookClient.fetchObject("me", JsonObject.class);
	  u.id=user.getString("id");
	  try{u.location=user.getJsonObject("location").getString("name");
	  List<JsonObject> mycountry=facebookClient.executeQuery("select current_location from user where uid=me()",JsonObject.class);
	  u.country=mycountry.get(0).getJsonObject("current_location").getString("country");
	  }catch(Exception e){e.printStackTrace();}
	  /*Fetching user's friends*/
	  try{
	  JsonObject friends1=facebookClient.fetchObject("me/friends&limit=100000",JsonObject.class);
	  friends=friends1.getJsonArray("data");
	  for(int i=0;i<friends.length();i++)
		  u.f.put(friends.getJsonObject(i).getString("id"),new Friend());
	  }catch(Exception e){e.printStackTrace();}
	  
	  /*Getting users relationship status*/
	  try{
	  String rstatus=user.getString("relationship_status");
	  u.rels=rstatus;
	  if(rstatus.equals("Married")||rstatus.equals("Engaged")||rstatus.equals("In a Relationship")) {
		  JsonObject who=user.getJsonObject("significant_other");
		  fr=u.f.get(who.getString("id"));
		  if(rstatus.equals("Married")) fr.inrelwith=0.7;
		  if(rstatus.equals("Engaged")) fr.inrelwith=0.6;
		  if(rstatus.equals("In a Relationship")) fr.inrelwith=0.5;
		  u.f.put(who.getString("id"),fr);
	  }
	  }catch(Exception e){e.printStackTrace();}
	  
	  String tmp="",t="";
	  JsonArray mutual=null;
	  JsonObject mutual1=null;
	  JsonMapper jsonMapper = new DefaultJsonMapper();
	  int i,j,k,l,totreq=(int)Math.ceil((double)friends.length()/50.0);
	  /*Getting mutual friends between all the users*/
	  try{
		  for(i=0,l=0,k=0;k<totreq;k++){
		  BatchRequest batch[]=new BatchRequest[Math.min(50,friends.length()-i)];
		  for(j=0;j<batch.length;j++,i++){
			  tmp=friends.getJsonObject(i).getString("id");
			  t="me/mutualfriends/"+tmp;//+"&limit=100000";
			  batch[j]=new BatchRequest.BatchRequestBuilder(t).build();
		  }
		  List<BatchResponse>batchResponses=facebookClient.executeBatch(batch);
		  for(j=0;j<batchResponses.size();j++,l++){
			try{
				mutual1=jsonMapper.toJavaObject(batchResponses.get(j).getBody(),JsonObject.class);
				mutual=mutual1.getJsonArray("data");
				tmp=friends.getJsonObject(l).getString("id");
				fr=u.f.get(tmp);
				fr.sharedfrnds=((double)mutual.length()/(double)friends.length())*3;
				u.f.put(tmp,fr);
			  }catch(Exception e){}
		  	}
		  }
	  }catch(Exception e){}
	  
	  /*Getting family relationships*/
	  JsonObject family1=facebookClient.fetchObject("me/family",JsonObject.class);
	  try{
	  JsonArray family=family1.getJsonArray("data");
	  for(i=0;i<family.length();i++)
	  {
		  fr=u.f.get(family.getJsonObject(i).getString("id"));
		  tmp=family.getJsonObject(i).getString("relationship");
		  if(tmp.equalsIgnoreCase("brother")||tmp.equalsIgnoreCase("sister")) fr.familyrels=0.3;
		  else if(tmp.equalsIgnoreCase("mother")||tmp.equalsIgnoreCase("father")) fr.familyrels=0.2;
		  else fr.familyrels=0.1;
		  u.f.put(family.getJsonObject(i).getString("id"),fr);
	  }
	  }catch(Exception e){e.printStackTrace();}
	  
	  
	  /*Getting geographic proximity*/
	  String temp1="",temp="";long dist=100000;List<JsonObject> friendloc=null;String myloc="";List<JsonObject>mylocation=null;
	  try{myloc=user.getJsonObject("location").getString("name");
	  friendloc=facebookClient.executeQuery("select uid,current_location from user where uid IN(select uid2 from friend where uid1=me())",JsonObject.class);
	  mylocation=facebookClient.executeQuery("select uid,current_location from user where uid=me()",JsonObject.class);
	  }catch(Exception e){}
	  for(i=0;i<friends.length();i++)
	  {
		  try{
			  tmp=friendloc.get(i).getJsonObject("current_location").getString("name");
		  }catch(Exception e){tmp="";}
		  if(tmp.equals("")) continue;
		  fr=u.f.get(friendloc.get(i).getString("uid"));
		  if(myloc.equalsIgnoreCase(tmp)){fr.geogp=0.2;u.f.put(friendloc.get(i).getString("uid"),fr);}
		  else{
			  try{
				  if(friendloc.get(i).getJsonObject("current_location").getString("state").equalsIgnoreCase(mylocation.get(0).getJsonObject("current_location").getString("name")))
					  fr.geogp=0.1;
				  else if(friendloc.get(i).getJsonObject("current_location").getString("country").equalsIgnoreCase(mylocation.get(0).getJsonObject("current_location").getString("country")))
					  fr.geogp=0.05;
			  }catch(Exception e){}
		  }
		  u.f.put(friendloc.get(i).getString("uid"),fr);
	  }
	  /*Getting phototags */
	  
	  int pht_fcnt=0,pht_ftot=0,pht_mytot=0;
	  totreq=(int)Math.ceil((double)friends.length()/50.0);
	  temp="SELECT object_id FROM photo_tag WHERE subject="+u.id;
	  List<JsonObject>mytags=facebookClient.executeQuery(temp,JsonObject.class);
	  HashSet<String>tags=new HashSet<String>();
	  for(i=0;i<mytags.size();i++) tags.add(mytags.get(i).getString("object_id"));
	  pht_mytot=tags.size();
	  try{
	  for(i=0,l=0,k=0;k<totreq;k++)
	  {
		  BatchRequest batch[]=new BatchRequest[Math.min(50,friends.length()-i)];
		  for(j=0;j<batch.length;j++,i++){
			  t="fql?q=SELECT object_id FROM photo_tag WHERE subject="+friends.getJsonObject(i).getString("id");
			  batch[j]=new BatchRequest.BatchRequestBuilder(t).build();
		  }
		  List<BatchResponse>batchResponses=facebookClient.executeBatch(batch);
		  for(j=0,pht_fcnt=0;j<batchResponses.size();j++,l++,pht_fcnt=0){
				try{
					mutual1=jsonMapper.toJavaObject(batchResponses.get(j).getBody(),JsonObject.class);
					mutual=mutual1.getJsonArray("data");
					pht_ftot=mutual.length();
					try{
						  for(int z=0;z<pht_ftot;z++) 
							  if(tags.contains(mutual.getJsonObject(z).getString("object_id"))) 
								  pht_fcnt++;
						  if(pht_fcnt>0)
							  System.out.println(pht_fcnt);
					}catch(Exception e){pht_ftot=0;}
					tmp=friends.getJsonObject(l).getString("id");
					fr=u.f.get(tmp);
					fr.phototags=((double)pht_fcnt/(double)(pht_mytot+pht_ftot-pht_fcnt))*6;
					u.f.put(tmp,fr);
				  }catch(Exception e){}
			   if(pht_ftot==0) continue;
		  }
	  }
	  }catch(Exception e){}


	  /*Getting shared groups*/
	  
	  int grp_fcnt=0;int flag=0;
	  totreq=(int)Math.ceil((double)friends.length()/50.0);
	  List<JsonObject>mygrps=facebookClient.executeQuery("select gid from group_member where uid=me()",JsonObject.class);
	  HashSet<String>mygrpids=new HashSet<String>();
	  BatchRequest batch[]=null;l=0;
	  try{
	  for(i=0;i<mygrps.size();i++) mygrpids.add(mygrps.get(i).getString("gid"));
	  }catch(Exception e){mygrpids=null;flag=1;}
	  if(flag==0)
	  for(i=0,l=0,k=0;k<totreq;k++){
		  batch=new BatchRequest[Math.min(50,friends.length()-i)];
		  for(j=0;j<batch.length;j++,i++){
			  t="fql?q=select gid from group_member where uid="+friends.getJsonObject(i).getString("id");
			  batch[j]=new BatchRequest.BatchRequestBuilder(t).build();
	      }
	  
	  List<BatchResponse>batchResponses=facebookClient.executeBatch(batch);
	  for(j=0;j<batchResponses.size();j++,l++){
		  try{
			  	grp_fcnt=0;
				mutual1=jsonMapper.toJavaObject(batchResponses.get(j).getBody(),JsonObject.class);
				mutual=mutual1.getJsonArray("data");
				tmp=friends.getJsonObject(l).getString("id");
				try{
					  for(int z=0;z<mutual.length();z++) 
						  if(mygrpids.contains(mutual.getJsonObject(z).getString("gid"))) 
							  grp_fcnt++;
				}catch(Exception e){grp_fcnt=0;}
				if(grp_fcnt!=0){
					fr=u.f.get(friends.getJsonObject(l).getString("id"));
					fr.sharedgrps=((double)grp_fcnt/(double)(mutual.length()+mygrps.size()))*3;
					u.f.put(friends.getJsonObject(l).getString("id"),fr);
				}
		  }catch(Exception e){}
	  }  
	  }  
	  
	  /*Getting wallposts*/
	  JsonObject wallposts1=facebookClient.fetchObject("me/statuses&limit=10000000",JsonObject.class);
	  JsonArray wallposts=wallposts1.getJsonArray("data");
	  HashMap<String,Integer>likes=new HashMap<String,Integer>();
	  HashMap<String,Integer>comment=new HashMap<String,Integer>();
	  for(i=0;i<friends.length();i++){likes.put(friends.getJsonObject(i).getString("id"),0);comment.put(friends.getJsonObject(i).getString("id"),0);}
	  for(i=0;i<wallposts.length();i++)
	  {
		  try{
			  JsonObject post=wallposts.getJsonObject(i).getJsonObject("comments");
			  JsonArray comm=post.getJsonArray("data");
			  for(j=0;j<comm.length();j++)
			  {
				  temp=comm.getJsonObject(j).getJsonObject("from").getString("id");
				  try{comment.put(temp,((comment.get(temp).intValue())+1));}catch(Exception e){}
			  }
		  }catch(Exception e){}
		  try{
			  JsonObject like=wallposts.getJsonObject(i).getJsonObject("likes");
			  JsonArray liked=like.getJsonArray("data");
			  for(j=0;j<liked.length();j++)
			  {
				  temp=liked.getJsonObject(j).getString("id");
				  try{likes.put(temp,((likes.get(temp).intValue())+1));}catch(Exception e){}
			  }
		  }catch(Exception e){}
	  }
	  for(i=0;i<friends.length();i++) {
		  temp=friends.getJsonObject(i).getString("id");
		  fr=u.f.get(temp);
		  fr.wallposts=((double)(comment.get(temp)+likes.get(temp))/(double)wallposts.length())*2;
		  u.f.put(temp,fr);
	  }
  }
}