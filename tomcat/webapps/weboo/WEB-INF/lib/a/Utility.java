package a;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;
import java.util.zip.GZIPInputStream;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;

public class Utility{
	public static String normalize(String arg){
		arg=removeHtml(arg);
		arg=stopFilter(arg,Configuration.STOP_FILE);
		arg=Stemmer.Stem(arg);
		arg=arg.replace("null","");
		return arg;
	}
	
	public static String tokenizeBetween(String s,String s1,String s2) {
        String s3=null;
        try {
            String as[]=s.split(s1);
            String as1[]=as[1].split(s2);
            s3=as1[0];
        } catch (Exception ex) {ex.printStackTrace();return null;}
        return s3;
    }
	public static Map sortByComparator(Map unsortMap,char ch) {
        List list = new LinkedList(unsortMap.entrySet());
        if(ch=='d')
        Collections.sort(list, new Comparator() {
        	public int compare(Object o1, Object o2) {
        		return (-1)*((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        if(ch=='a')
        	Collections.sort(list, new Comparator() {
            	public int compare(Object o1, Object o2) {
            		return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
                }
            });
        Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
	     	Map.Entry entry = (Map.Entry)it.next();
	     	sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	public static boolean updatedb(String n,int i){
		try{
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection conn=DriverManager.getConnection(Configuration.DBCONNSTRING,"sa","");
			Statement st=conn.createStatement();
			System.out.println("Updating the database for values n="+n+" and i="+i);
			String q="update doneprofiles set done="+String.valueOf(i)+" where username="+n;
			st.executeUpdate(q);
			conn.close();
			return true;
		}catch(Exception e){}
		return false;
	}
	public static String executeGet(String targetURL,String host,char ch){
		URL url;
	    HttpURLConnection connection=null;  
	    try {
	      url = new URL(targetURL);
	      connection=(HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("GET");
		  connection.setRequestProperty("Host",host);
	      connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
	      connection.setRequestProperty("Accept-Language","en-US,en;q=0.8");
	      if(ch=='c') connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	      if(ch=='i') connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; Media Center PC 4.0; InfoPath.2; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; ShopperReports 3.1.22.0; SRS_IT_E879047EB0765B5336AF90)");
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	      GZIPInputStream gzis = new GZIPInputStream(connection.getInputStream());
	      InputStreamReader reader = new InputStreamReader(gzis);
	      BufferedReader in = new BufferedReader(reader);
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = in.readLine()) != null) {
	    	  response.append(line);
	    	  response.append('\r');
	      }
	      in.close();
	      return response.toString();
	    } catch (Exception e) {e.printStackTrace();return null;}
	}
	public static String excutePost(String targetURL, String urlParameters,String host)
	{
	    URL url;
	    HttpURLConnection connection=null;  
	    try {
	      url = new URL(targetURL);
	      connection=(HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Length","" + Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	      connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		  connection.setRequestProperty("Host",host);
	      connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
	      connection.setRequestProperty("Accept-Language","en-US,en;q=0.8");
	      connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");		
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes(urlParameters);
	      wr.flush();
	      wr.close();
	      GZIPInputStream gzis = new GZIPInputStream(connection.getInputStream());
	      InputStreamReader reader = new InputStreamReader(gzis);
	      BufferedReader in = new BufferedReader(reader);
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = in.readLine()) != null) {
	    	  response.append(line);
	    	  response.append('\r');
	      }
	      in.close();
	      return response.toString();
	    } catch (Exception e) {e.printStackTrace();return null;} 
	}
	
	public static String getId(String accToken){
		FacebookClient facebookClient1=new DefaultFacebookClient(accToken);
		List<JsonObject> userid=facebookClient1.executeQuery("select uid from user where uid=me()",JsonObject.class);
		return userid.get(0).getString("uid");
	}
    public static String removeHtml(String arg) {
		String s="";
		try{
        s=arg.replaceAll("</?\\w+((\\s+\\w+(\\s*=\\s*(?:\".*?\"|'.*?'|[^'\">\\s]+))?)+\\s*|\\s*)/?>"," ");
		s=s.replaceAll("http://[a-zA-Z0-9.-@]+.(com|org|net|info|us|in)","");
		s=s.replaceAll("www.[a-zA-Z0-9.-@]+.(com|org|net|info|us|in|uk)","");
		s=s.replaceAll("[^\\p{ASCII}]", "");
		s=s.replaceAll("[-.,\\(\\)\\[\\]\\{\\}\"':;?!&\\/`]"," ");
		s=s.replaceAll("\\s+", " ");
        //System.out.println("text:\n" + s);
		}catch(Exception e){}
		/*String s = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô";
		String s=textHandler.toString();
	    s=s.replaceAll("[æèéêë]","e");s=s.replaceAll("[üûùú]","u");s=s.replaceAll("[íìïî]","i");
		s=s.replaceAll("[àááâ]","a");s=s.replaceAll("ôöóò","o");s=s.replaceAll("[ÈÉÊË]","E");
		s=s.replaceAll("[ÛÙ]","U"); s=s.replaceAll("[ÏÎ]","I");s=s.replaceAll("[ÀÂ]","A");
		s=s.replaceAll("Ô","O");s=s.replaceAll("ç","c");s=s.replaceAll("ÿ","y");s=s.replaceAll("ñ","n");
		System.out.println(s);*/
		return s;
    }
	public static String stopFilter(String input,String stopfile) 
	{
		String output="",tk="";
		try{
		StringTokenizer st=new StringTokenizer(input);
		HashSet<CaselessString>stop=new HashSet<CaselessString>();
		BufferedReader br=new BufferedReader(new FileReader(stopfile));
		while((tk=br.readLine())!=null)
			stop.add(CaselessString.as(tk));
		while(st.hasMoreTokens())
		{
			tk=st.nextToken();
			output+=(!stop.contains(CaselessString.as(tk))?(tk+" "):"");
		}
		}catch(Exception e){e.printStackTrace();}
		return output;
	}
	/*public static void main(String args[]){
		String inp="<a href=\"htpp://adsf.com>f</a>\"<dl><dd>http://www.gogle.com Constructs a www.facebook.com string tokenizer for the specified string. All characters in the <code>delim</code> argument are the delimiters for separating tokens. <p> If the <code>returnDelims</code> flag is <code>true</code>, then  the delimiter characters are also returned as tokens. Each  delimiter is returned as a string of length one. If the flag is <code>false</code>, the delimiter characters are skipped and only serve as separators between tokens. </p><p> Note that if <tt>delim</tt> is <tt>null</tt>, this constructor does not throw an exception. However, trying to invoke other methods on the resulting <tt>StringTokenizer</tt> may result in a  <tt>NullPointerException</tt>.</p><p></p></dd><dt><b>Parameters:</b></dt><dd><code>str</code> - a string to be parsed.</dd><dd><code>delim</code> - the delimiters.</dd><dd><code>returnDelims</code> - flag indicating whether to return the delimiters          as tokens.</dd></dl>";
		inp=normalize(inp);
		System.out.println(inp);
		inp=stopFilter(inp,"stop-list.txt");
		System.out.println(inp);
	}*/
}