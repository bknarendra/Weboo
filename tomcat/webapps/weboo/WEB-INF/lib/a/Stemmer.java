package a;
import java.util.StringTokenizer;
import java.util.HashMap;
import edu.smu.tspell.wordnet.*;

public class Stemmer {
	static WordNetDatabase database;
	public static HashMap<String,String>AllWords;
	public static String Stem(String sentence)
	{
		AllWords=new HashMap<String,String>();
		StringTokenizer st=new StringTokenizer(sentence);
		String out="",tk="",stemmedword="";
		Synset[] synsets;
		System.setProperty("wordnet.database.dir",Configuration.DICTIONARY_FILE);
		WordNetDatabase database=WordNetDatabase.getFileInstance();
		while(st.hasMoreTokens()){
			tk=st.nextToken();
			if(containsNumbers(tk)){out+=stemmedword+" ";continue;}
			stemmedword=AllWords.get(tk);
			if(stemmedword!=null){out+=stemmedword+" ";continue;}
			synsets=database.getSynsets(tk);
			if(synsets.length>0)
			{
				String[] wordForms=synsets[0].getWordForms();
				out+=wordForms[0]+" ";
				AllWords.put(tk,wordForms[0]);
			}
			else {
				AllWords.put(tk,tk);
				out+=tk+" ";
			}
		}
		return out;
	}
	public static boolean containsNumbers(String w) {
		return ((w.indexOf('1')!=-1)||(w.indexOf('2')!=-1)||(w.indexOf('3')!=-1)||(w.indexOf('4')!=-1)||(w.indexOf('5')!=-1)||(w.indexOf('6')!=-1)||(w.indexOf('7')!=-1)||(w.indexOf('8')!=-1)||(w.indexOf('9')!=-1)||(w.indexOf('0')!=-1));
	}
	/*public static void main(String args[]){
		System.out.println(Stem("wrote smarter Google facebook wrote"));
	}*/
}