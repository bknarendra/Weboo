package a;

import java.io.*;
import java.net.*;
public class AddLike {
	public static boolean add(String tag,String accessToken){
		String des="";
		try{
			User u=ReaderWriter.readProfile(Utility.getId(accessToken));
			Likes l=new Likes();
			System.out.println("searching freebase");
			Freebase freebase=Freebase.getFreebase();
			JSON result=freebase.search(tag);
			if(!result.get("result").has(1)){
				try{tag=DidYouMean.didYouMean(tag);}catch(Exception ex){ex.printStackTrace();}
				result=freebase.search(tag);
			}
			l.name=result.get("result").get(0).get("name").string();
			int tot=result.get("result").get(0).get("type").array().size();
			String cat="";
			for(int i=0;i<tot;i++){
				cat=result.get("result").get(0).get("type").get(i).get("name").string();
				if(cat.equals("Topic")) continue;
				else break;
			}
			l.fbcat=cat;
			l.mycat=UserSimilarity.getLikeCat(cat);
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
			l.id=l.name;
			l.desc+=" "+temp1;
			u.mylikes.add(l);
			return true;
		}catch(Exception e){e.printStackTrace();}
		return false;
	}
	public static void main(String[] args) {
		try {
			add("Footboll","AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");
		} catch (Exception e){e.printStackTrace();}
	}
}
