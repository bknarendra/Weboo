package a;
import java.sql.*;

public class MainController {
	public static int ch=0;
	public static int i=0;
	public static String gname="";
	public static String accessToken="";
	public static void calculate(String name,String accessToken,int i){
		while(i!=2){
			try{
			if(i==0){if(SocialStrength.callSS(accessToken)){i=1;Utility.updatedb(name,i);}}
			//System.in.read();
			if(i==1){if(UserSimilarity.callUS(accessToken)){i=2;Utility.updatedb(name,i);}}
			//System.in.read();
			}catch(Exception e){}
		}
	}
	public static void main(String[] args){
		try {
			/*Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			int i=0;String name,q="";
			Connection conn=DriverManager.getConnection(Configuration.DBCONNSTRING,"sa","");
			while(Boolean.TRUE){
				Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("select * from doneprofiles");
				while(rs.next()){
					i=rs.getInt(2);
					if(i<2) {
						gname=rs.getString(1);
						name=rs.getString(1);
						q="select * from user where username='"+name+"'";
						rs=st.executeQuery(q);
						if(rs.next())
							accessToken=rs.getString(3);
						if(accessToken.length()>0) calculate(name,accessToken,i);
					}
				}
				ch=0;
				while(Boolean.TRUE) {
					Thread.sleep(10000);
					if(ch!=0) break;
				}
			}
			conn.close();*/
			if(args.length==2){gname=args[0];accessToken=args[1];
				calculate(args[0],args[1],0);}
		} catch (Exception e) {e.printStackTrace();return;}
	}
}