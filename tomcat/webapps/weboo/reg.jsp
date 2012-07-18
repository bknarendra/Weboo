<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.io.*,java.net.*,java.security.Security.*,com.sun.net.ssl.*,com.sun.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
</head>
<body>
<%
String uname=request.getParameter("name");
String passwd=request.getParameter("pwd");
String accesstoken=request.getParameter("access_token");
{
	try{
		accesstoken=accesstoken.substring(accesstoken.indexOf("access_token=")+13,accesstoken.indexOf("&expires"));
		System.out.println(accesstoken);
		try{
				System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
				java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); 
				String ur="https://graph.facebook.com/oauth/access_token?client_id=394720130562592&client_secret=5bc65473ae7ba41c52685ff3c4d464d7&grant_type=fb_exchange_token&fb_exchange_token="+accesstoken;
				URL url=new URL(ur);
				HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");
				connection.setFollowRedirects(true);
				connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println("outloop");
				String temp1="",temp="";
				if(connection.getResponseCode()==200&&connection.getResponseMessage().equals("OK")){
					while((temp=br.readLine())!=null){temp1+=temp;}
					accesstoken=temp1.substring(temp1.indexOf("access_token=")+13,temp1.indexOf("&expires"));
				}
				else {System.out.println("error occurred");accesstoken="";out.println("Error in obtaining accesstoken");}
			}catch(Exception e){e.printStackTrace();accesstoken="";out.println("Error in registration");}
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
		} catch (Exception e) {System.err.println("ERROR: failed to load HSQLDB JDBC driver.");e.printStackTrace();return;}
		Connection conn=DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
		Statement p1;
		p1=conn.createStatement();
		String query="insert into user values('"+uname+"','"+passwd+"','"+accesstoken+"')";
		p1.executeUpdate(query);
		query="insert into doneprofiles values('"+uname+"',0)";p1.executeUpdate(query);
		conn.close();
		if(accesstoken.length()>0){
		String cmd="cmd /c start run.bat "+uname+" "+accesstoken;
		try{Runtime.getRuntime().exec(cmd);}catch(Exception e){e.printStackTrace();}}
		out.println("Registration successful....");
		response.sendRedirect("index.html");
	}
	catch(Exception e)
	{out.println("Error in registration");}
}
%>
</body>
</html>
