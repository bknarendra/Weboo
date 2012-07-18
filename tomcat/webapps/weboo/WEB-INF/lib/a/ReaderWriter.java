package a;
import java.io.*;
public class ReaderWriter {
	public static User readProfile(String id) throws ClassNotFoundException,FileNotFoundException,IOException{
		User user=null;
		String path=Configuration.FILEPATH+id;
		ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
		user=(User)objectIn.readObject();
		objectIn.close();
		return user;
	}
	public static boolean writeProfile(User u) throws ClassNotFoundException,FileNotFoundException,IOException{
		String path=Configuration.FILEPATH+u.id;
		ObjectOutputStream objectOut=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
		objectOut.writeObject(u);
		objectOut.close(); 
		return true;
	}
	public static void main(String[] args) {
		
	}
}
