package a;
import java.util.*;
import java.io.*;
public class Movies implements Serializable{
	public HashMap<String,Integer>genre;
	public HashMap<String,Integer>actor;
	public HashMap<String,Integer>director;
	public HashMap<String,String>name;
	public Movies(){name=new HashMap<String,String>();genre=new HashMap<String,Integer>();actor=new HashMap<String,Integer>();director=new HashMap<String,Integer>();}
}
