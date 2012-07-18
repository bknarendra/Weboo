package a;
import java.util.*;
public class MyLikeSimilarity {
	public static LinkedHashMap<String, Double> calcLike(User u)
	{
		int k,num=u.mylikes.size(),i,j,fnum=0;
		Vector<String>docs;
        Object[]frnds=u.flikes.keySet().toArray();
        String tmp="";
        Vector<Likes>fr;
        LinkedHashMap<String, Double>usersims=new LinkedHashMap<String,Double>();
        double sum=0;
        for(i=0;i<frnds.length;i++){
        	for(j=0,sum=0;j<num;j++){
        		docs=new Vector<String>();
        		tmp=u.mylikes.elementAt(j).mycat;
        		docs.add(u.mylikes.elementAt(j).desc);
        		fr=u.flikes.get(frnds[i]);
        		fnum=fr.size();
        		for(k=0;k<fnum;k++){
        			if(fr.elementAt(k).mycat.equalsIgnoreCase(tmp)) docs.add(fr.elementAt(k).desc);
        		}
        		sum+=LikeSimilarity.sim(docs);
        	}
        	usersims.put((String)frnds[i],(sum/(double)num));
        }
        return usersims;
	}
	/*public static double sim(Vector<String>doc){
		HashMap<String,Integer>terms=new HashMap<String,Integer>();
		HashMap<String,Double>idfs=new HashMap<String,Double>();
		int i,num=doc.size(),j,cmatch=0;
		StringTokenizer st;String tmp;
		for(i=0;i<num;i++) {
        	st=new StringTokenizer(doc.elementAt(i));
        	while(st.hasMoreTokens()){
        		tmp=st.nextToken();
        		if(terms.containsKey(tmp)) terms.put(tmp,((terms.get(tmp).intValue())+1));
        		else terms.put(tmp,1);
        	}
        }
		HashMap<String,Integer>d1=new HashMap<String,Integer>();
		HashMap<String,Integer>d2=new HashMap<String,Integer>();
		Object[]totterm=terms.keySet().toArray();
		for(i=0;i<totterm.length;i++) {
			idfs.put((String)totterm[i],Math.log(num/(double)(1+terms.get(totterm[i]))));
			d1.put((String)totterm[i],0);d2.put((String)totterm[i],0);
		}
		st=new StringTokenizer(doc.elementAt(0));
		double dd1[]=new double[totterm.length],dd2[]=new double[totterm.length];
		while(st.hasMoreTokens()){
			tmp=st.nextToken();
			if(d1.containsKey(tmp)) d1.put(tmp,((d1.get(tmp).intValue())+1));
		}
		for(i=0;i<totterm.length;i++) dd1[i]=d1.get(totterm[i]).doubleValue()*idfs.get(totterm[i]).doubleValue();
		try{
			for(i=1;i<num;i++){
				st=new StringTokenizer(doc.elementAt(i));
				while(st.hasMoreTokens()){
					tmp=st.nextToken();
					if(d2.containsKey(tmp)) d2.put(tmp,((d2.get(tmp).intValue())+1));
				}
				for(j=0;j<totterm.length;j++) dd2[i]=d2.get(totterm[i]).doubleValue()*idfs.get(totterm[i]).doubleValue();
				if(cosineSim(dd1,dd2)>0.499999) cmatch++; 
				for(j=0;j<totterm.length;j++) d2.put((String)totterm[i],0);
			}
		}catch(Exception e){e.printStackTrace();return 0.0;}
		return ((double)cmatch/(double)(num-1));
	}
	public static double cosineSim(double d1[],double d2[]){
		int i;double dot,sum1,sum2;
		for(i=0,dot=0;i<d1.length;i++) dot+=(d1[i]*d2[i]);
		for(i=0,sum1=0,sum2=0;i<d1.length;i++) {sum1+=d1[i]*d1[i];sum2+=d2[i]*d2[i];}
		return (dot/(Math.sqrt(sum1)*Math.sqrt(sum2)));
	}
	public static void main(String[] args) {
		//User u=new User();
	}
*/
}
