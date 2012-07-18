<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.io.*,java.net.*"%>
<%
	if (request.getParameter("q").equals("movie"))
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("movie.json")));
		String s1="",s="";
		while((s1=br.readLine())!=null) s+=s1;
		out.println(s);
	} else if (request.getParameter("q").equals("music"))
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("music.json")));
		String s1="",s="";
		while((s1=br.readLine())!=null) s+=s1;
		out.println(s);
	}
%>