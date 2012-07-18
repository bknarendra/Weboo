<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,org.h2.security.*;" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
	String name=request.getParameter("username");
	String pass=request.getParameter("pass");
	try{
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
		} catch (Exception e) {System.err.println("ERROR: failed to load HSQLDB JDBC driver.");e.printStackTrace();return;}
		Connection conn=DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("select * from user where username='"+name+"'");
		if(rs.next()){
			String accesstoken=rs.getString(3);
			out.println("accesstoken="+accesstoken+"&endofaccesstoken");
		}
		else response.sendRedirect("index.html");
		conn.close();
	}
	catch(Exception e) {System.out.println(e);}
%>
</body>
</html>