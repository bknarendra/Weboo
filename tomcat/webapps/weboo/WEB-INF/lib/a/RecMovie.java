package a;
import java.io.Serializable;
import java.util.Vector;

public class RecMovie implements Serializable{
	public String img,name,imdbid,story,director;
	double rating;
	public Vector<String>genres;
	public Vector<String>cast;
	public RecMovie(){rating=0;img="";name="";imdbid="";story="";director="";genres=new Vector<String>();cast=new Vector<String>();}
}
