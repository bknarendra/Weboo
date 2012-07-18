package a;
import java.util.*;
import net.sf.jtmdb.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.*;
import com.restfb.types.*;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Track;

import java.lang.*;
import java.io.*;
import java.net.*;
public class UserSimilarity {
  public final FacebookClient facebookClient;
  public static User u;
  HashSet<String>lname;
  Vector<HashSet<String>>flname;
  HashSet<String>lcat;
  static JsonObject info;
  static Movie movie;
  Vector<HashSet<String>>flcat;
  
  public static String curid;
  public static int flag=0;
  public static boolean callUS(String accessToken){
      String uid=Utility.getId(accessToken);
      try {
    	  u=ReaderWriter.readProfile(uid);
      } catch(FileNotFoundException e){Utility.updatedb(MainController.gname,0);MainController.i=0;e.printStackTrace();return false;}
		catch(ClassNotFoundException e) {e.printStackTrace();return false;} 
		catch(IOException e) {e.printStackTrace();return false;}
	  UserSimilarity usersimi=new UserSimilarity(accessToken);
	  if(usersimi.fetchLikes()) return true;
	  return false;
  }
  
  public static void main(String[] args){
	  	  FacebookClient facebookClient1=new DefaultFacebookClient("AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");
          JsonObject user=facebookClient1.fetchObject("me", JsonObject.class);
          String uid=user.getString("id");
         try {
			u=ReaderWriter.readProfile(uid);
		} catch (FileNotFoundException e){flag=1;e.printStackTrace();}
		catch (ClassNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
          callUS("AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");
  }
	UserSimilarity(String accessToken) {
		facebookClient=new DefaultFacebookClient(accessToken);
		if(flag==1) u=new User();
		lname=new HashSet<String>();flname=new Vector<HashSet<String>>();lcat=new HashSet<String>();flcat=new Vector<HashSet<String>>();
	}
	public boolean fetchLikes(){
		Likes lik;
		String temp="";
		User v;
		JsonObject user=facebookClient.fetchObject("me", JsonObject.class);
		u.id=user.getString("id");
		JsonObject ulikes1=facebookClient.fetchObject("me/likes&limit=100000", JsonObject.class);
		JsonArray ulikes=ulikes1.getJsonArray("data");
		for(int i=0;i<ulikes.length();i++) {
			v=u;
			temp="like"+u.id;
			u.mylikes.add(new Likes(ulikes.getJsonObject(i).getString("name"),ulikes.getJsonObject(i).getString("id"),ulikes.getJsonObject(i).getString("category"),getLikeCat(ulikes.getJsonObject(i).getString("category")),temp,getDesc(ulikes.getJsonObject(i).getString("category"),ulikes.getJsonObject(i).getString("id"),1,ulikes.getJsonObject(i).getString("name"))));
			try {
				if(getLikeCat(ulikes.getJsonObject(i).getString("category")).equals("Movies")) 
					addMovie(u.id,ulikes.getJsonObject(i).getString("name"),ulikes.getJsonObject(i).getString("category"));
				else if(ulikes.getJsonObject(i).getString("category").equalsIgnoreCase("MoviesRelated"))
				{
					Movies m=u.mymovies;
					if(m.actor.containsKey(ulikes.getJsonObject(i).getString("name"))) m.actor.put(ulikes.getJsonObject(i).getString("name"),(m.actor.get(ulikes.getJsonObject(i).getString("name")).intValue()+1));
					else if(m.director.containsKey(ulikes.getJsonObject(i).getString("name"))) m.director.put(ulikes.getJsonObject(i).getString("name"),(m.director.get(ulikes.getJsonObject(i).getString("name")).intValue()+1));
					else m.actor.put(ulikes.getJsonObject(i).getString("name"),1);
					u.mymovies=m;
				}
			} catch (Exception e){e.printStackTrace();}
		}
		
		Vector<Likes>fl;
		JsonObject friends1=null;
		try{
		friends1=facebookClient.fetchObject("me/friends&limit=100000",JsonObject.class);}catch(Exception frnde){frnde.printStackTrace();try{Thread.sleep(1000);}catch(Exception thrd){}
		friends1=facebookClient.fetchObject("me/friends&limit=100000",JsonObject.class);}
		JsonArray friends=friends1.getJsonArray("data");
		for(int i=0;i<friends.length();i++){
			u.flikes.put(friends.getJsonObject(i).getString("id"),new Vector<Likes>());
			u.fmovies.put(friends.getJsonObject(i).getString("id"),new Movies());
			u.fmusic.put(friends.getJsonObject(i).getString("id"),new Music());
		}
		for(int i=0;i<friends.length();i++) {
			
			temp=friends.getJsonObject(i).getString("id")+"/likes&limit=100000";
			try{ulikes1=facebookClient.fetchObject(temp,JsonObject.class);}catch(Exception likes){likes.printStackTrace();try{Thread.sleep(1000);}catch(Exception thrd){}
			ulikes1=facebookClient.fetchObject(temp,JsonObject.class);}
			ulikes=ulikes1.getJsonArray("data");
			fl=new Vector<Likes>();
			for(int j=0;j<ulikes.length();j++) 
			{
				System.out.println(i+" "+j);
				temp="like"+friends.getJsonObject(i).getString("id");
				curid=friends.getJsonObject(i).getString("id");
				fl.add(new Likes(ulikes.getJsonObject(j).getString("name"),ulikes.getJsonObject(j).getString("id"),ulikes.getJsonObject(j).getString("category"),getLikeCat(ulikes.getJsonObject(j).getString("category")),temp,getDesc(ulikes.getJsonObject(j).getString("category"),ulikes.getJsonObject(j).getString("id"),2,ulikes.getJsonObject(j).getString("name"))));
				try {
					if(getLikeCat(ulikes.getJsonObject(j).getString("category")).equalsIgnoreCase("Movies"))
						addMovie(friends.getJsonObject(i).getString("id"),ulikes.getJsonObject(j).getString("name"),ulikes.getJsonObject(i).getString("category"));
					else if(ulikes.getJsonObject(j).getString("category").equalsIgnoreCase("MoviesRelated"))
					{
						Movies m=u.fmovies.get(friends.getJsonObject(i).getString("id"));
						if(m.actor.containsKey(ulikes.getJsonObject(j).getString("name"))) m.actor.put(ulikes.getJsonObject(j).getString("name"),(m.actor.get(ulikes.getJsonObject(j).getString("name")).intValue()+1));
						else if(m.director.containsKey(ulikes.getJsonObject(j).getString("name"))) m.director.put(ulikes.getJsonObject(j).getString("name"),(m.director.get(ulikes.getJsonObject(j).getString("name")).intValue()+1));
						else m.actor.put(ulikes.getJsonObject(j).getString("name"),1);
						u.fmovies.put(friends.getJsonObject(i).getString("id"),m);
					}
				} catch (Exception e) {e.printStackTrace();}
			}
			u.flikes.put(friends.getJsonObject(i).getString("id"),fl);
		}
		
		u.simusers=MyLikeSimilarity.calcLike(u);
		try{ReaderWriter.writeProfile(u);return true;}catch(Exception e){}
		return false;//System.out.println(u.simusers);
	}
	public static void addMovie(String id,String name,String cat) throws Exception{
		int i,ac=0,di=0;
		Movies m;
		String imdbID="";
		String gen[]=null,act[]=null,dir[]=null;
		if(cat.equalsIgnoreCase("TV Show")){
			if(!info.getString("Genre").equals("N/A")) gen=info.getString("Genre").split(", ");
			if(!info.getString("Actors").equals("N/A")) act=info.getString("Actors").split(", ");
			if(!info.getString("Director").equals("N/A")) dir=info.getString("Director").split(", ");
			if(!info.getString("imdbID").equals("N/A")) imdbID="S"+info.getString("imdbID");
		}
		else{
			imdbID="M"+movie.getImdbID();
			Set<Genre>genr=movie.getGenres();
			Object genre[]=genr.toArray();
			gen=new String[genre.length];
			for(int k=0;k<gen.length;k++) gen[k]=((Genre)genre[k]).getName();
			Set<CastInfo>cast1=movie.getCast();
			Object cast[]=cast1.toArray();
			act=new String[cast.length];
			dir=new String[cast.length];
			
			for(int k=0;k<act.length;k++){
				if(((CastInfo)cast[k]).getJob().equals("Actor"))
					act[ac++]=((CastInfo)cast[k]).getName();
				else if(((CastInfo)cast[k]).getJob().equals("Director"))
					dir[di++]=((CastInfo)cast[k]).getName();
			}
		}
		if(id.equals(u.id)) m=u.mymovies;
		else m=u.fmovies.get(id);
		m.name.put(name,imdbID);
		try{
			for(i=0;i<gen.length;i++) {
				if(m.genre.containsKey(gen[i]))
					m.genre.put(gen[i],(m.genre.get(gen[i]).intValue()+1));
				else m.genre.put(gen[i],1);
			}	
		}catch(Exception e){}
		try{
			for(i=0;i<act.length;i++) {
				if(m.actor.containsKey(act[i]))
					m.actor.put(act[i],(m.actor.get(act[i]).intValue()+1));
				else m.actor.put(act[i],1);
			}
		}catch(Exception e){e.printStackTrace();}
		try{
			for(i=0;i<dir.length;i++) {
				if(m.director.containsKey(dir[i]))
					m.director.put(dir[i],(m.actor.get(dir[i]).intValue()+1));
				else m.director.put(dir[i],1);
			}
		}catch(Exception e){e.printStackTrace();}
		if(id.equals(u.id)) u.mymovies=m;
		else u.fmovies.put(id,m);
	}
	public static JsonObject getInfo(String name){
		String temp,temp1="";
		JsonObject info=null;
		try{
			  String ur="http://www.imdbapi.com/?i=&t="+URLEncoder.encode(name,"UTF-8");
			  URL url=new URL(ur);
			  HttpURLConnection uc=(HttpURLConnection)url.openConnection();
			  uc.setRequestMethod("GET");
			  uc.setDoOutput(true);
			  uc.setDoInput(true);
			  BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			  while((temp=br.readLine())!=null){temp1+=temp;}
			  info=new JsonObject(temp1);
		  }catch(Exception e){e.printStackTrace();}
		  return info;
	}
	public String getDesc(String cat,String lid,int who,String name){
		String des="";
		if(getLikeCat(cat).equals("Movies")){
			if(name.endsWith("Movie")) name.replace("Movie","");
			if(name.endsWith("movie")) name.replace("movie","");
			if(cat.equalsIgnoreCase("TV Show")){
				info=getInfo(name);
				try{des+=" "+info.getString("Title");}catch(Exception e){}
				try{des+=" "+info.getString("Genre");}catch(Exception e){}
				try{if(!info.getString("Actors").equals("N/A"))des+=" "+info.getString("Actors");}catch(Exception e){}
				try{if(!info.getString("Director").equals("N/A"))des+=" "+info.getString("Director");}catch(Exception e){}
				try{des+=" "+info.getString("Plot");}catch(Exception e){}
			}
			else{
				try{
					GeneralSettings.setApiKey(Configuration.TMDBKEY);
					List<Movie>mov=Movie.search(name);
					movie=Movie.getInfo(mov.get(0).getID());
					Set<Genre>gen=movie.getGenres();
					Object genset[]=gen.toArray();
					for(int k=0;k<genset.length;k++) des+=" "+((Genre)genset[k]).getName();
					Set<CastInfo>cast1=movie.getCast();
					Object cast[]=cast1.toArray();
					for(int k=0;k<cast.length;k++) des+=" "+((CastInfo)cast[k]).getName();
				}catch(UnknownHostException e){}
				catch(Exception e){e.printStackTrace();}
			}
		}
		else if(getLikeCat(cat).equalsIgnoreCase("Music")){
			Music mus=null;
			if(cat.equalsIgnoreCase("Album")) {
				try{Collection<Album> albums=Album.search(name,Configuration.LASTFMKEY,"1");
				for(Album al:albums){
					des+=" "+al.getArtist();
					des+=" "+al.getName();
					des+=" "+cat;
					if(who==1){
						if(u.mymusic.album.containsKey(al.getName())) u.mymusic.album.put(al.getName(),(u.mymusic.album.get(al.getName()).intValue()+1));
						else u.mymusic.album.put(al.getName(),1);
						u.mymusic.name.add(name);
					}
					if(who==2){
						try{
						mus=u.fmusic.get(curid);
						if(mus.album.containsKey(al.getName())) mus.album.put(al.getName(),(mus.album.get(al.getName()).intValue()+1));
						else mus.album.put(al.getName(),1);
						mus.name.add(name);
						u.fmusic.put(curid,mus);
						}catch(Exception e){e.printStackTrace();}
					}
				}
				}catch(Exception e){des+=name;}
			}
			else if(cat.equalsIgnoreCase("Musician/Band")) {
				try{mus=null;
				Artist art=Artist.getInfo(name,Configuration.LASTFMKEY);
				des+=" "+art.getName();
				Collection<String>tags=art.getTags();
				for(String tag:tags)des+=" "+tag;
				des+=" "+cat;
				if(who==1){
					if(u.mymusic.artist.containsKey(art.getName())) u.mymusic.artist.put(art.getName(),(u.mymusic.artist.get(art.getName()).intValue()+1));
					else u.mymusic.artist.put(art.getName(),1);
					u.mymusic.name.add(name);
				}
				if(who==2){
					try{mus=u.fmusic.get(curid);
					if(mus.artist.containsKey(art.getName())) mus.artist.put(art.getName(),(mus.album.get(art.getName()).intValue()+1));
					else mus.artist.put(art.getName(),1);
					mus.name.add(name);
					u.fmusic.put(curid,mus);
					}catch(Exception e){e.printStackTrace();}
				}
				}catch(Exception e){des+=name;}
			}
			else if(cat.equalsIgnoreCase("Song")){
				try{mus=null;
				Collection<Track> tracks=Track.search(name,Configuration.LASTFMKEY);
				for(Track tr:tracks){
					Track trck=Track.getInfo(tr.getArtist(),tr.getName(),Configuration.LASTFMKEY);
					des+=" "+trck.getName();
					des+=" "+trck.getAlbum();
					des+=" "+trck.getArtist();
					des+=" "+cat;
					try{
					if(who==1) mus=u.mymusic;
					if(who==2) mus=u.fmusic.get(curid);
					if(mus.artist.containsKey(trck.getArtist())) mus.artist.put(trck.getArtist(),(mus.artist.get(trck.getArtist()).intValue()+1));
					else mus.artist.put(trck.getArtist(),1);
					if(mus.album.containsKey(trck.getAlbum())) mus.album.put(trck.getAlbum(),(mus.album.get(trck.getAlbum()).intValue()+1));
					else mus.album.put(trck.getAlbum(),1);
					if(mus.track.containsKey(trck.getName())) mus.track.put(trck.getName(),(mus.track.get(trck.getName()).intValue()+1));
					else mus.track.put(trck.getName(),1);
					if(who==1) u.mymusic=mus;
					if(who==2) u.fmusic.put(curid, mus);
					}catch(Exception e){e.printStackTrace();}
					break;
				}
				}catch(Exception e){des+=name;}
			}
		}
		else{
			JsonObject desc=null;
		try{
			String ur="http://graph.facebook.com/"+lid;
			URL url=new URL(ur);
			HttpURLConnection uc=(HttpURLConnection)url.openConnection();
			uc.setRequestMethod("GET");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			BufferedReader br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String temp1="",temp="";
			while((temp=br.readLine())!=null){temp1+=temp;}
			desc=new JsonObject(temp1);
		}catch(Exception e){}
		try{des+=" "+desc.getString("plot_outline");}catch(Exception e){}
		try{des+=" "+desc.getString("description");}catch(Exception e){}
		try{des+=" "+desc.getString("about");}catch(Exception e){}
		try{des+=" "+desc.getString("company_overview");}catch(Exception e){}
		try{des+=" "+desc.getString("general_info");}catch(Exception e){}
		try{des+=" "+desc.getString("products");}catch(Exception e){}
		try{des+=" "+desc.getString("personal_info");}catch(Exception e){}
		try{des+=" "+desc.getString("bio");}catch(Exception e){}
		try{des+=" "+cat;}catch(Exception e){}
		
		if(des.length()<(cat.length()+100)){
			try{
				String temp1="",t1="";
				try{temp1=searchFreebase(desc.getString("name"));}
				catch(Exception e){
					try{
					t1=DidYouMean.didYouMean(desc.getString("name"));}catch(Exception exa){}
					if(t1.length()==0) temp1="";
					else {
						name=t1;
						try{temp1=searchFreebase(name);}catch(Exception ex){}
					}
				}
			des+=" "+temp1;
			}catch(Exception e){}
		}}
		des=Utility.normalize(des);
		return des;
	}
	public static String searchFreebase(String name) throws Exception{
		System.out.println("searching freebase");
		Freebase freebase=Freebase.getFreebase();
		JSON result=freebase.search(name);
		String link=result.get("result").get(0).get("article").get("id").string();
		String ur="http://api.freebase.com/api/trans/blurb"+link+"?maxlength=1500";
		URL url=new URL(ur);
		HttpURLConnection uc=(HttpURLConnection)url.openConnection();
		uc.setRequestMethod("GET");
		uc.setDoOutput(true);
		uc.setDoInput(true);
		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String temp1="",temp="";
		while((temp=br.readLine())!=null){temp1+=temp;}
		return temp1;
	}
	public static String getLikeCat(String cat){
		if(cat.equalsIgnoreCase("TV Program")||cat.equalsIgnoreCase("Film")||cat.equalsIgnoreCase("Movie")||cat.equalsIgnoreCase("TV Show")||cat.equalsIgnoreCase("Movies/Music"))
			return "Movies";
		else if(cat.indexOf("Actor")!=-1||cat.indexOf("Director")!=-1||cat.equalsIgnoreCase("Film Director")||cat.equalsIgnoreCase("Actor/Director")||cat.equalsIgnoreCase("Fictional Character")||cat.equalsIgnoreCase("Producer")||cat.equalsIgnoreCase("Writer")||cat.equalsIgnoreCase("Entertainer"))
			return "MoviesRelated";
		else if(cat.indexOf("Rapper")!=-1||cat.indexOf("Album")!=-1||cat.equalsIgnoreCase("Song")||cat.equalsIgnoreCase("Musician/Band")||cat.equalsIgnoreCase("Music Video"))
			return "Music";
		else return "Rest";
	}
}