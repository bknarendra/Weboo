package a;
import java.io.*;
import java.net.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
public class DidYouMean {
	public static String didYouMean(String s){
		String word="";
		String url="http://www.google.co.in/search?hl=en&q="+URLEncoder.encode(s);
		String html=Utility.executeGet(url,"www.google.co.in",'i');
		//System.out.println(html);
		Document content=Jsoup.parse(html);
		Element submitted=null;
		try{
			submitted=content.getElementById("ires").clone();
			submitted=submitted.getElementsContainingText("Showing results for").first().clone();
			submitted=submitted.getElementsByTag("a").first().clone();
			word=submitted.text();
		}catch(Exception e){}//System.out.println("error in did you mean");}	
		return word;
	}
	public static void main(String args[]){
		//System.out.println(didYouMean("footbll"));
	}
}
