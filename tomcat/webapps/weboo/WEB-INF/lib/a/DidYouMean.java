import java.io.*;
import java.net.*;
import org.jsoup.*;
import java.util.zip.*;
import org.jsoup.nodes.*;
import org.jsoup.examples.HtmlToPlainText;
public class DidYouMean {
	public static String didYouMean(String s){
		String word="";
		String url="http://www.google.co.in/search?hl=en&q="+URLEncoder.encode(s);
		String html=Utility.executeGet(url,"www.google.co.in",'i');
		Document content=Jsoup.parse(html);
		Element submitted=null;
		try{
			submitted=content.getElementById("topstuff").clone();
			HtmlToPlainText h=new HtmlToPlainText();
			word=h.getPlainText(submitted);
			int q,p=word.indexOf("Did you mean:");
			if(p>=0){
				word=word.substring(p+"Did you mean:".length());
				p=word.indexOf("<>");
				if(p>0) word=word.substring(0,p);
				word=word.trim();
			}
			else{
				p=word.indexOf("Showing results for");
				if(p>=0){
					word=word.substring(p+"Showing results for".length());
					p=word.indexOf("<>");
					if(p>0) word=word.substring(0,p);
					word=word.trim();
				}
				else return "No results";
			}
		}catch(Exception e){e.printStackTrace();}	
		return word;
	}
	public static void main(String args[]){
		System.out.println(didYouMean(args[0]));
	}
}