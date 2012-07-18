package a;
import java.util.*;
import java.io.*;
public class Music implements Serializable {
	public HashMap<String,Integer>artist;
	public HashMap<String,Integer>track;
	public HashMap<String,Integer>album;
	public HashSet<String>name;
	public Music(){artist=new HashMap<String,Integer>();album=new HashMap<String,Integer>();track=new HashMap<String,Integer>();name=new HashSet<String>();}
}
