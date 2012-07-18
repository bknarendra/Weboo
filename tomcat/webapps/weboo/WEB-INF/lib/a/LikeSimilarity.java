package a;
import java.io.IOException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import java.util.*;
import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.commons.math.linear.SparseRealVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LikeSimilarity {
	public static RAMDirectory idx;
    public static double sim(Vector<String>doc) {
    	int cmatch=0,num=0,i,j;
        try {
        	idx=new RAMDirectory();
            IndexWriter writer =
            	new IndexWriter(idx,new WhitespaceAnalyzer(Version.LUCENE_CURRENT), true, IndexWriter.MaxFieldLength.LIMITED);
            num=doc.size();
            for(j=0;j<num;j++) writer.addDocument(createDocument(doc.elementAt(j)));
            writer.optimize();
            writer.close();
            IndexReader reader=IndexReader.open(idx);
            Map<String,Integer>terms=new HashMap<String,Integer>();
            TermEnum termEnum=reader.terms(new Term("content"));
            int pos=0;
            while(termEnum.next()) {
            	Term term = termEnum.term();
              	if (!"content".equals(term.field())) 
            	  	break;
              	terms.put(term.text(), pos++);
            }
            int docids=reader.numDocs();
            DocVector[] docs=new DocVector[docids];
            for(i=0;i<docids;i++)
            {
            	if(!reader.isDeleted(i))
            	{
            		TermFreqVector[]tfvs = reader.getTermFreqVectors(i);
            		docs[i]=new DocVector(terms);
            		for(TermFreqVector tfv : tfvs) {
            			String[] termTexts = tfv.getTerms();
            			int[] termFreqs = tfv.getTermFrequencies();
            			for(j=0;j<termTexts.length;j++) {
            				docs[i].setEntry(termTexts[j],termFreqs[j]);
            			}
            		}
            		docs[i].normalize();
        	    }
            }
            for(j=1;j<num;j++) if(getCosineSimilarity(docs[0], docs[j])>0.49999999999)cmatch++;
            reader.close();
        }
        catch(Exception pe) {
            pe.printStackTrace();
        }
        if(num>1) return ((double)cmatch/(double)(num-1));
        else return 0;
    }
    	  
    static double getCosineSimilarity(DocVector d1, DocVector d2) {
        return (d1.vector.dotProduct(d2.vector)) /
        (d1.vector.getNorm() * d2.vector.getNorm());
    }

    static class DocVector {
    		  public Map<String,Integer> terms;
    		  public SparseRealVector vector;
    		  int who;
    	    public DocVector(Map<String,Integer>terms) {
    	      this.terms = terms;
    	      this.vector = new OpenMapRealVector(terms.size());
    	    }
    	    
    	    public void setEntry(String term, int freq) {
    	      if (terms.containsKey(term)) {
    	        int pos = terms.get(term);
    	        vector.setEntry(pos, (double) freq);
    	      }
    	    }
    	    
    	    public void normalize() {
    	      double sum = vector.getL1Norm();
    	      vector = (SparseRealVector) vector.mapDivide(sum);
    	    }
    	    
    	    public String toString() {
    	    RealVectorFormat formatter = new RealVectorFormat();
        	return formatter.format(vector);
        }
    }
    private static Document createDocument(String content) {
        Document doc = new Document();
        doc.add(new Field("content", content, Store.YES, Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS));
        return doc;
    }
}
