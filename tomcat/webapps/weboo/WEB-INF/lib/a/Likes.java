package a;
import java.io.Serializable;


public class Likes implements Serializable
{
	public String name,id,fbcat,mycat,sspacefile,desc;
	public Likes(){name="";id="";fbcat="";mycat="";sspacefile="";desc="";}
	public Likes(String n,String i,String fbc,String myc,String ssf,String des){name=n;id=i;fbcat=fbc;mycat=myc;sspacefile=ssf;desc=des;}
}