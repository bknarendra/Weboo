import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.*;
import com.restfb.*;
import com.restfb.json.*;
import java.io.*;
import java.awt.Dimension;
import javax.swing.Scrollable;


public class Frames implements WindowFocusListener,HyperlinkListener{
	JFrame frame;
	JEditorPane jep;
	public static String url;
	public static void main(String[] args) {
		//Frames d=new Frames(0,0,"inbox");
		//System.out.println(SWTWebKitBrowser.accessToken.length());
	}
	public String norm(String str){
		int size=30,pos=0;
		StringBuilder work=new StringBuilder(str);
		while((pos=work.indexOf(" ",pos+size))>=0)
		    work.setCharAt(pos, '\n');
		return work.toString();
	}
	public Frames(int x,int y,String type)
	{
		try{
		frame=new JFrame();
		frame.setUndecorated(true);
		frame.setSize(320,400);
		jep=new JEditorPane();
		jep.setBounds(0,0,320,400);
		DefaultFacebookClient facebookClient=new DefaultFacebookClient(SWTWebKitBrowser.accessToken);
		jep.setContentType("text/html");
		String content="<html><body><table width=\"294\" border=\"1\">";
		String names="";
		if(type.equals("feed")){
			try{
				List<JsonObject> feed=facebookClient.executeQuery("SELECT actor_id,message,description,permalink FROM stream WHERE filter_key in (SELECT filter_key FROM stream_filter WHERE uid=me() AND type='newsfeed') AND is_hidden=0 limit 6",JsonObject.class);
				if(feed.size()>0){
					names=feed.get(0).getString("actor_id");
					for(int i=1;i<feed.size();i++)
						names+=","+feed.get(i).getString("actor_id");
					String query="select name from profile where id IN ("+names+")";
					System.out.println(query);
					List<JsonObject>actors=facebookClient.executeQuery(query,JsonObject.class);
					for(int i=0;i<feed.size();i++){
						content+="<tr><td>"; 
						content+="<a href=\"http://www.facebook.com/"+feed.get(i).getString("actor_id")+"\">"+actors.get(i).getString("name")+"</a><br/><pre>";
						if(feed.get(i).getString("message")!=null&&!feed.get(i).getString("message").equals("")&&!feed.get(i).getString("message").equals("null")) content+=norm(feed.get(i).getString("message"))+". ";
						if(feed.get(i).getString("description")!=null&&!feed.get(i).getString("description").equals("")&&!feed.get(i).getString("description").equals("null")) content+=norm(feed.get(i).getString("description"))+" ";
						if(feed.get(i).getString("permalink")!=null&&!feed.get(i).getString("permalink").equals("")&&!feed.get(i).getString("permalink").equals("null")) content+="<a href=\""+feed.get(i).getString("permalink")+"\">Link</a>";
						content+="</td></tr>";
					}
				}
			}catch(Exception e){e.printStackTrace();}
		}
		else if(type.equals("inbox")){
			JsonArray all=null;
			try{
				//BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("m")));
				//String s="",s1="";
				//while((s1=br.readLine())!=null) s+=s1;
				JsonObject message=facebookClient.fetchObject("me/inbox&limit=6",JsonObject.class);
				//JsonObject message=new JsonObject(s);
				all=message.getJsonArray("data");
				for(int i=0;i<all.length()&&i<10;i++){try{
					content+="<tr><td><a href=\"http://www.facebook.com/"+all.getJsonObject(i).getJsonObject("to").getJsonArray("data").getJsonObject(1).getString("id")+"\">"+all.getJsonObject(i).getJsonObject("to").getJsonArray("data").getJsonObject(1).getString("name")+"</a> -> <a href=\"http://www.facebook.com/"+all.getJsonObject(i).getJsonObject("to").getJsonArray("data").getJsonObject(0).getString("id")+"\">"+all.getJsonObject(i).getJsonObject("to").getJsonArray("data").getJsonObject(0).getString("name")+"</a><br/><pre>";
					content+=norm(all.getJsonObject(i).getJsonObject("comments").getJsonArray("data").getJsonObject(0).getString("message"))+" <a href=\"http://www.facebook.com/messages/"+all.getJsonObject(i).getJsonObject("to").getJsonArray("data").getJsonObject(1).getString("id")+"\">Link</a>";
					content+="</td></tr>";}catch(Exception e){}
				}
			}catch(Exception e){e.printStackTrace();}
		}
		else if(type.equals("notifications")){
			try{
				JsonObject notifications=facebookClient.fetchObject("me/notifications&limit=6",JsonObject.class);
				JsonArray all=notifications.getJsonArray("data");
				if(all.length()==0) content+="<tr><td>No new notifications";
				for(int i=0;i<all.length();i++){
					content+="<tr><td>";
					content+="<a href=\"http://www.facebook.com/"+all.getJsonObject(i).getJsonObject("from").getString("id")+"\">"+all.getJsonObject(i).getJsonObject("from").getString("name")+"</a><br/><pre>";
					content+=norm(all.getJsonObject(i).getString("title"))+" <a href=\""+all.getJsonObject(i).getString("link")+"\">Link</a>";
				}
			}catch(Exception e){e.printStackTrace();}
		}
		content+="</table></body></html>";
		jep.setText(content);
		jep.setEditable(false);
		jep.addHyperlinkListener(this);
		JScrollPane scrollpane=new JScrollPane(jep);
		frame.add(scrollpane);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setLocation(x,y);
		url="";
		frame.addWindowFocusListener(this);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void windowGainedFocus(WindowEvent arg0) {}
	public void windowLostFocus(WindowEvent arg0) {
		frame.dispose();
		url="done";
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				url=event.getURL().toString();
			} catch(Exception e){e.printStackTrace();}
		}
	}

}



//feed: /*fql?q=SELECT actor_id, message,description,permalink FROM stream WHERE filter_key in (SELECT filter_key FROM stream_filter WHERE uid=me() AND type='newsfeed') AND is_hidden = 0*/

//inbox:/*fql?q={"first_msg":"SELECT author_id,message_id, thread_id, body, created_time FROM message WHERE thread_id IN (SELECT thread_id FROM thread WHERE folder_id = 0) ORDER BY created_time DESC LIMIT 10","users":"select name from user where uid IN(select author_id from #first_msg)"}*/

//notifications:/*me/notifications*/

//getting name/*fql?q=select name from user where uid IN (SELECT actor_id FROM stream WHERE filter_key in (SELECT filter_key FROM stream_filter WHERE uid=me() AND type='newsfeed') AND is_hidden = 0)*/