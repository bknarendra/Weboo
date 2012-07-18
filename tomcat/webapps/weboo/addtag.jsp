<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.io.*,java.net.*,a.*"%>
<%
	String tag=request.getParameter("tag"),accessToken=request.getParameter("accessToken");
	System.out.println(tag+" "+accessToken);
	if(AddLike.add(tag,accessToken))
		out.println("done");
	else out.println("error");
%>