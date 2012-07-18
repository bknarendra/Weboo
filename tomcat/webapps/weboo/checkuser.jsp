<%@ page import="java.sql.*" %> 
<%
String name=request.getParameter("name").toString();
System.out.println(name);
String data ="";
try{
	try {
		Class.forName("org.hsqldb.jdbc.JDBCDriver" );
	} catch (Exception e) {System.err.println("ERROR: failed to load HSQLDB JDBC driver.");e.printStackTrace();return;}
	Connection con=DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
	Statement st=con.createStatement();
    ResultSet rs=st.executeQuery("select * from user where username='"+name+"'");
	int count=0;
    while(rs.next())
        count++;
    if(count>0) data="Not Available";
    else data="Available";
out.println(data);
System.out.println(data);
con.close();
}
catch(Exception e) {System.out.println(e);}
%>