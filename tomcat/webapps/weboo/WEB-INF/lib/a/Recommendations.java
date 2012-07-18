package a;
import java.util.*;
import net.sf.jtmdb.*;
import com.restfb.json.*;
import com.restfb.types.*;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Track;

import java.io.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class Recommendations {
	public static HashMap<String,String>imdbids=new HashMap<String,String>();
	public static User u;
	public static HashMap<String,Double>ratings=new HashMap<String,Double>();
	public static void addAllM(HashMap<String,String>allm){
		Set<String>allmo=allm.keySet();
		for(String m:allmo)	imdbids.put(m,allm.get(m));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String movieRec(String accessToken){
		int k=0;
		try {
			u=ReaderWriter.readProfile(Utility.getId(accessToken));
			u.simusers=(LinkedHashMap<String, Double>) Utility.sortByComparator(u.simusers,'d');
			u.socialstrength=(LinkedHashMap<String, Double>) Utility.sortByComparator(u.socialstrength,'d');
			k=Math.min(10,u.simusers.size());
			HashMap<String,Integer>t=u.mymovies.genre;
			HashMap<String,Double>allgen=new HashMap<String,Double>();
			for (Map.Entry entry : t.entrySet()) {
	        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(1.5*((Integer)entry.getValue()).doubleValue())));
	        	else allgen.put((String)entry.getKey(),1.5*((Integer)entry.getValue()).doubleValue());
	        }
			double z=0;
			Set<String>simfrnds=u.simusers.keySet();int i=0;
			for(String simfrnd:simfrnds){
				t=u.fmovies.get(simfrnd).genre;
				for(Map.Entry entry : t.entrySet()) {
					z=(1-(i*(1.0/k)));
		        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(z*((Integer)entry.getValue()).doubleValue())));
		        	else allgen.put((String)entry.getKey(),z*((Integer)entry.getValue()).doubleValue());
		        }
				i++;
				if(i>=k) break;
			}
			Set<String>socfrnds=u.socialstrength.keySet();i=0;
			for(String socfrnd:socfrnds){
				t=u.fmovies.get(socfrnd).genre;
				for(Map.Entry entry : t.entrySet()) {
					z=(1-(i*(1.0/k)));
		        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(z*((Integer)entry.getValue()).doubleValue())));
		        	else allgen.put((String)entry.getKey(),z*((Integer)entry.getValue()).doubleValue());
		        }
				i++;
				if(i>=k) break;
			}
			allgen=(HashMap<String,Double>)Utility.sortByComparator(allgen,'d');
			BrowseOptions b=new BrowseOptions();
			b.setOrderBy(BrowseOptions.ORDER_BY.RELEASE);
			b.setOrder(BrowseOptions.ORDER.DESC);
			long l=52560*100000;
			b.setReleaseMax(new Date((System.currentTimeMillis()+l)));
			b.setPerPage(6);b.setPage(1);b.setRatingMin(5);
			Pair<Boolean, Set<Genre>>abc;
			int genc=Math.min(5,allgen.size());
			i=0;
			HashMap<String,String>mov=new HashMap<String,String>();
			try {
				GeneralSettings.setApiKey(Configuration.TMDBKEY);
				abc=Genre.getList();
				Set<String>popgenset=allgen.keySet();
				for(String genre:popgenset){
					b.clearGenres();
					b.addGenres(getGenreId(abc.getSecond(),genre));
					mov=mybrowse(b);
					addAllM(mov);
					i++;
					if(i==genc) break;
				}
				JsonObject json=null;
				Vector<RecMovie>recmovie=new Vector<RecMovie>();
				JSONArray allmovies=new JSONArray();
				JSONObject jb=null;
				RecMovie rec=null;
				double sc=0,maxval=-100;
				String url="http://www.imdb.com/widget/recommendations/_ajax/get_title_info",html="",params="",response="";
				Set<String>imdbidss=imdbids.keySet();
				HashMap<String,Double>ratings2=new HashMap<String,Double>();
				for(String movie:imdbidss){
					params="tconst="+movie+"&info=sims%3A"+movie+"&caller_name=p13nsims-title";
					response=Utility.excutePost(url,params,"www.imdb.com");
					json=new JsonObject(response);
					html=json.getString("html_title_info");
					rec=parseimdbinfo(html,movie);
					if(rec.img.equals("")) rec.img=imdbids.get(movie);
					for(i=0,sc=0;i<rec.genres.size();i++) if(allgen.containsKey(rec.genres.get(i))) sc+=allgen.get(rec.genres.get(i));
					if(maxval<sc) maxval=sc;
					ratings2.put(movie,sc);
					recmovie.add(rec);
				}
				for(String movie:imdbidss){
					sc=(ratings2.get(movie)/maxval)*10.0;
					ratings.put(movie,ratings.get(movie).doubleValue()+sc);
				}
				ratings=(HashMap<String,Double>)Utility.sortByComparator(ratings,'d');
				Set<String>ratedmovies=ratings.keySet();
				for(String rated:ratedmovies){
					for(i=0;i<recmovie.size();i++){if(recmovie.get(i).imdbid.equals(rated)) {rec=recmovie.get(i);rec.rating=ratings.get(rec.imdbid).doubleValue();break;}}
					jb=recmovieToJson(rec);
					allmovies.put(jb);
				}
				System.out.println(allmovies.toString());
				return allmovies.toString();
			} catch(Exception e){e.printStackTrace();}
			//String res=Utility.excutePost("http://www.imdb.com/widget/recommendations/_ajax/get_more_recs","count=25&start=0&specs=p13nsims%3Att2077833&caller_name=p13nsims-title","www.imdb.com");
			//System.out.println("done");
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		return "";
	}
	
	@SuppressWarnings("deprecation")
	public static String musicRec(String accessToken){
		int i=0,k=0;
		try {
			u=ReaderWriter.readProfile(Utility.getId(accessToken));
			u.simusers=(LinkedHashMap<String, Double>) Utility.sortByComparator(u.simusers,'d');
			u.socialstrength=(LinkedHashMap<String, Double>) Utility.sortByComparator(u.socialstrength,'d');
			k=Math.min(10,u.simusers.size());
			HashMap<String,Integer>t=u.mymusic.artist;
			HashMap<String,Double>allgen=new HashMap<String,Double>();
			for (Map.Entry entry : t.entrySet()) {
	        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(1.5*((Integer)entry.getValue()).doubleValue())));
	        	else allgen.put((String)entry.getKey(),1.5*((Integer)entry.getValue()).doubleValue());
	        }
			double z=0;
			Set<String>simfrnds=u.simusers.keySet();
			for(String simfrnd:simfrnds){
				t=u.fmusic.get(simfrnd).artist;
				for(Map.Entry entry : t.entrySet()) {
					z=(1-(i*(1.0/k)));
		        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(z*((Integer)entry.getValue()).doubleValue())));
		        	else allgen.put((String)entry.getKey(),z*((Integer)entry.getValue()).doubleValue());
		        }
				i++;
				if(i>=k) break;
			}
			Set<String>socfrnds=u.socialstrength.keySet();i=0;
			for(String socfrnd:socfrnds){
				t=u.fmusic.get(socfrnd).artist;
				for(Map.Entry entry : t.entrySet()) {
					z=(1-(i*(1.0/k)));
		        	if(allgen.containsKey(entry.getKey())) allgen.put((String)entry.getKey(),(allgen.get(entry.getKey()).doubleValue()+(z*((Integer)entry.getValue()).doubleValue())));
		        	else allgen.put((String)entry.getKey(),z*((Integer)entry.getValue()).doubleValue());
		        }
				i++;
				if(i>=k) break;
			}
			JSONArray allsongs=new JSONArray(),oneartist=null;
			allgen=(HashMap<String,Double>)Utility.sortByComparator(allgen,'d');
			k=Math.min(5,allgen.size());
			String url="",json="";Set<String>artist=allgen.keySet();
			for(String art:artist){
				url="http://tinysong.com/s/"+URLEncoder.encode(art)+"?format=json&key="+Configuration.TINYSONG+"&limit=7";
				json=Utility.executeGet(url,"tinysong.com",'c');
				oneartist=new JSONArray(json);
				for(i=0;i<oneartist.length();i++) allsongs.put(oneartist.getJSONObject(i));
				if(--k==0) break;
			}
			//System.out.println(allsongs.toString());
			return allsongs.toString();
		}catch(Exception e){}
		return "";
	}
	public static int getGenreId(Set<Genre>xyz,String name){
		for(Genre d:xyz) if(d.getName().equals(name)) return d.getID(); 
		return 0;
	}
	
	public static HashMap<String,String>mybrowse(BrowseOptions options) {
		if (GeneralSettings.getApiKey()!=null&&!GeneralSettings.getApiKey().equals("")) {
			try {String url="";
				for(Integer d:options.getGenres())
					url=GeneralSettings.BASE_URL+GeneralSettings.MOVIE_BROWSE_URL+ GeneralSettings.getAPILocaleFormatted() + "/"+ GeneralSettings.API_MODE_URL + "/"+ GeneralSettings.getApiKey() + "?order_by=release&order=desc&page="+String.valueOf(options.getPage().intValue())+"&per_page="+String.valueOf(options.getPerPage().intValue())+"&rating_min="+String.valueOf(options.getRatingMin().doubleValue())+"&rating_max="+String.valueOf(options.getRatingMax().doubleValue())+"&genres="+String.valueOf(d)+"&release_min=0&release_max="+String.valueOf(options.getReleaseMax().getTime());
				URL call = new URL(url);
				HashMap<String,String>movieimdb=new HashMap<String,String>();
				String jsonString = GeneralSettings.Utilities.readUrlResponse(call).trim();
				if ((jsonString.startsWith("[") || jsonString.startsWith("{"))&&!jsonString.equals("[\"Nothing found.\"]")) {
				JSONArray jsonArray = new JSONArray(jsonString.toString());
				String abc="",imgp="";double rating=0;
				for (int i=0;i<jsonArray.length();i++) {
					try{abc=(String)jsonArray.getJSONObject(i).get("imdb_id");rating=((Double)jsonArray.getJSONObject(i).get("rating")).doubleValue();imgp=(String)jsonArray.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");}catch(Exception e){}
					movieimdb.put(abc,imgp);
					ratings.put(abc,rating);
				}
			}
			return movieimdb;
			} catch (Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	public static RecMovie parseimdbinfo(String html,String imdbid){
		Document content=Jsoup.parse(html);
		Element submitted=null;
		RecMovie mov=new RecMovie();
		mov.imdbid=imdbid;
		//get title
		try{
		submitted=content.getElementsByClass("rec-title").first().clone();
		submitted=submitted.getElementsByTag("a").first().clone();
		String title=submitted.text();
		//System.out.println(title);
		mov.name=title;
		}catch(Exception e){System.out.println("error in getting title");}
		//get genres
		try{
			submitted = content.getElementsByClass("rec-cert-genre").first().clone();
			submitted.getElementsByTag("img").remove();
			String genres[]=submitted.text().split("[|]");
			for(int gen=0;gen<genres.length;gen++) genres[gen]=genres[gen].trim();
			for(int gen=0;gen<genres.length;gen++) mov.genres.add(genres[gen]);//System.out.println(genres[gen]);
			
		}catch(Exception e){System.out.println("error in getting genres");}
		//get story
		try{
		String story=content.getElementsByClass("rec-outline").first().text();
		//System.out.println(story);
		mov.story=story;
		}catch(Exception e){System.out.println("error in getting story");}
		//get director
		try{
			submitted = content.getElementsByClass("rec-director").first().clone();
			submitted.getElementsByTag("b").remove();
			String director=submitted.text();
			if(director.indexOf(",")!=-1) director=director.substring(0,director.indexOf(","));
			//System.out.println(director);
			mov.director=director;
		}catch(Exception e){System.out.println("error in getting director");}
		//get cast
		try{
			submitted=content.getElementsByClass("rec-actor").first().clone();
			submitted=submitted.getElementsByTag("span").first();
			submitted.getElementsByTag("b").remove();
			String actorinfo=submitted.text();
			String actors[]=actorinfo.split("and");
			for(int i=0;i<actors.length;i++) actors[i]=actors[i].trim();
			for(int i=0;i<actors.length;i++) mov.cast.add(actors[i]);//System.out.println(actors[i]);
		}catch(Exception e){System.out.println("error in getting actors");}
		//get image
		try{
			submitted=content.getElementsByClass("rec_poster_img").first().clone();
			String img=submitted.attr("style");
			img=Utility.tokenizeBetween(img,"\\(http://",".jpg\\)");
			img="http://"+img+".jpg";
			//System.out.println(img);
			mov.img=img;
		}catch(Exception e){System.out.println("error in getting image");mov.img="";}
		return mov;
	}
	public static JSONObject recmovieToJson(RecMovie re){
		//String json="";
		JSONObject j=new JSONObject();
		try {
			j=j.put("name",re.name);
			j=j.put("director",re.director);
			j=j.put("imdbid",re.imdbid);
			j=j.put("img",re.img);
			j=j.put("story",re.story);
			j=j.put("genre",re.genres);
			j=j.put("rating",re.rating);
			j=j.put("cast",re.cast);
		} catch (JSONException e) {	e.printStackTrace();}
		return j;
	}
	public static void main(String[] args) {
		//movieRec("AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");
		musicRec("AAAFmZCvEqZBiABANPz0ObdhprG8EnnCYoRmteZCJbzBe4JEqXSEjcIZAqsaLIfvbH8kaVsEkkZCjZAXkVmLSHlnhQ2q4R7gimkg3rxkn8W3QZDZD");
	}
}
