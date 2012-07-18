package a;
import java.io.Serializable;
import java.util.*;
public class User implements Serializable{
	public String id;
	public String rels,location,country;
	public HashMap<String,Friend>f;
	public Vector<Likes>mylikes;
	public Movies mymovies;
	public HashMap<String,Vector<Likes>>flikes;
	public HashMap<String,Movies>fmovies;
	public Music mymusic;
	public HashMap<String,Music>fmusic;
	public LinkedHashMap<String,Double>simusers;
	public LinkedHashMap<String,Double>socialstrength;
 	User(){id="";rels="";location="";country="";socialstrength=new LinkedHashMap<String,Double>();mymusic=new Music();simusers=new LinkedHashMap<String,Double>();fmusic=new HashMap<String,Music>();mymovies=new Movies();fmovies=new HashMap<String,Movies>();f=new HashMap<String,Friend>();mylikes=new Vector<Likes>();flikes=new HashMap<String,Vector<Likes>>();}
}