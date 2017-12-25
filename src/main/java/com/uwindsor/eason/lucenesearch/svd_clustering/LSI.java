package com.uwindsor.eason.lucenesearch.svd_clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.Term;
import org.la4j.Matrix;
import org.la4j.decomposition.SingularValueDecompositor;
import org.la4j.matrix.*;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.matrix.sparse.CCSMatrix;



public class LSI {
	
	public static  void main(String[] args) throws IOException, ParseException{
		String indexPath = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod_index";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
	    StandardAnalyzer analyzer = new StandardAnalyzer();
	    analyzer.setVersion(Version.LUCENE_6_6_1);
	    DirectoryReader reader = DirectoryReader.open(dir);
	    float N = 1000 ;
	    ArrayList<Document> documents = new ArrayList<Document>();
	    IndexSearcher isearcher = new IndexSearcher(reader);
	    // Parse a simple query that searches for "text":
	     QueryParser parser = new QueryParser("publisher", analyzer);
	    String q = "sigmod";
	   //for(int in = 0; in < ireader.maxDoc(); in++){
		  //q = ireader.document(in).get("id");
		  
		  System.out.println("q:"+q);
		  Query query = parser.parse(q);
		    ScoreDoc[] hits = isearcher.search(query, 500).scoreDocs;
		   System.out.println("total hits:"+hits.length);
		   for(int i = 0; i < hits.length; i ++ ){
			   documents.add(reader.document(hits[i].doc));
		   }
		   q = "icse";
		   query = parser.parse(q);
		   hits = isearcher.search(query, 500).scoreDocs;
		   System.out.println("total hits:"+hits.length);
		   for(int i = 0; i < hits.length; i ++ ){
			   documents.add(reader.document(hits[i].doc));
		   }
		   
	    //
	    System.out.println(documents.size());
	    for(int i = 0; i < documents.size(); i ++ ){
	    	System.out.println(documents.get(i).get("publisher"));
	    }
	    //System.exit(0);
	    //String path1 = "/Users/yaxin/Documents/IR/larger graph/icse_id.txt";
	    //String path2 = "/Users/yaxin/Documents/IR/larger graph/vldb_id.txt";
	     
	    
	    HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("an");
		set.add("are");
		set.add("as");
		set.add("at");
		set.add("be");
		set.add("but");
		set.add("by");
		set.add("for");
		set.add("if");
		set.add("in");
		set.add("into");
		set.add("is");
		set.add("it");
		set.add("no");
		set.add("not");
		set.add("of");
		set.add("on");
		set.add("or");
		set.add("such");
		set.add("that");
		set.add("the");
		set.add("their");
		set.add("then");
		set.add("there");
		set.add("these");
		set.add("they");
		set.add("this");
		set.add("to");
		set.add("was");
		set.add("will");
		set.add("with");
	    //通过title的term添加
//		String path= "/Users/yaxin/Documents/IR/NB/icse_vs_vldb/norm_rmsw/fs/icse_vldb_train6_mi_uni.txt";
//		FileReader myFileReader=new FileReader(path);
//		BufferedReader myBufferedReader=new BufferedReader(myFileReader);

		//loading terms from title field, put each term into vocab,
		IndexReader reader2 = DirectoryReader.open(dir);
		LuceneDictionary ld = new LuceneDictionary( reader2, "title" );
		BytesRefIterator iterator = ld.getEntryIterator();
		BytesRef byteRef = null;

		ArrayList<String> vocab = new ArrayList<String>();

		int c = 0;
		while ( ( byteRef = iterator.next() ) != null )
		{
			String term = byteRef.utf8ToString();
			vocab.add(term);
			if(c>5000) break;
		}

		//caculating the idf value
		System.out.println(vocab.size());
		ArrayList<Double> idf = new ArrayList<Double>();
		ArrayList<String> vocabulary = new ArrayList<String>();
		for(int i = 0; i < vocab.size(); i ++ ){
			//obtain the original Tf value stored by Lucene
			double df = reader.docFreq(new Term("title",vocab.get(i)));
			if(df>5){
				vocabulary.add(vocab.get(i));
				//calculate the idf value
				idf.add((double) (Math.log10(N/df)));
				System.out.println(vocab.get(i)+"\t"+ df+"\t"+Math.log10(N/df));
			}
			
			
		}
	    
		//double[][] table = new double[vocab.size()][reader.maxDoc()];
		ArrayList<Document> docs = new ArrayList<Document>();
		int sn=0;
		
		for(int i = 0; i < documents.size(); i ++ ){
			boolean flag = false;
			String[] split = documents.get(i).get("title").split(" ");
			for(int k = 0; k < split.length; k ++ ){					
				int index = vocabulary.indexOf(split[k]);
				if(index==-1) continue;				
				flag = true;
				sn++;
				System.out.println(sn);
			}
			if(flag) docs.add(documents.get(i));
		}
		
		sn=0;
		Matrix tfidf=new Basic2DMatrix(vocabulary.size(),docs.size());
		for(int i = 0; i < docs.size(); i ++ ){ 
			System.out.println("calc the Col doc "+i);
			boolean flag = false;
			String[] split = docs.get(i).get("title").split(" ");
			for(int k = 0; k < split.length; k ++ ){					
				int index = vocabulary.indexOf(split[k]);
				if(index==-1) continue;
				
				double temp = tfidf.get(index, i);
				System.out.println(temp);
				tfidf.set(index, i, temp+idf.get(index));
				sn++;
				System.out.println(sn);
			}
		}
		
		//Matrix tfidf= new Matrix(table);
		
		System.out.println("docs:"+docs.size());
		System.out.println("vocab:"+vocabulary.size());
		svd(tfidf,vocabulary,docs);      
		
		FileWriter fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/smtrix.txt");
		FileWriter fw1 = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/1-0.txt");
		for(int i = 0; i < tfidf.rows(); i ++ ){
			for(int j = 0; j < tfidf.columns(); j ++){
				if(tfidf.get(i, j)>1e-10||tfidf.get(i, j)<-1e-10){
					fw.write(i+"\t"+j+"\t"+tfidf.get(i, j)+"\n");
					fw1.write(i+"\t"+j+"\t1"+"\n");
				}
			}
			
		}
	    fw1.close();
		fw.close();
		
		fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/vocabulary.txt");
		for(int i = 0; i < vocabulary.size(); i ++ ){
			fw.write(i+"\t"+vocabulary.get(i)+"\n");
		}
		fw.close();
		
		fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/document.txt");
		for(int i = 0; i < docs.size(); i ++ ){
			fw.write(i+"\t"+docs.get(i).get("docId")+"\t"+docs.get(i).get("publisher")+"\n");
		}
		fw.close();
	    
	}
	
	 private static void svd(Matrix tfidf,ArrayList<String> vocabulary, ArrayList<Document> docs) throws IOException {
	        // get the three matrices
	        SingularValueDecompositor svd = new SingularValueDecompositor(tfidf);
	        Matrix[] K = svd.decompose();
	        
	        Matrix U = K[0];
	        Matrix D = K[1];
	        Matrix V = K[2];
	       

	        // print their dimensions for checking
	        System.out.println("U: " + dim(U));
	        System.out.println("D: " + dim(D));
	        System.out.println("V: " + dim(V));

	        // number of largest singular values to be considered
	        
	       
	        
	        
	        FileWriter fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/u.txt");
	       
	        for(int i = 0; i < U.rows(); i ++){
	        	//fw.write(vocabulary.get(i)+"\t");
	        	for(int j = 0; j < U.columns(); j ++ ){
	        		fw.write(U.get(i, j)+"\t");
	        	}
	        	fw.write("\n");
	        }
	        fw.close();
	        
	        fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/d.txt");
	        for(int i = 0; i < D.rows(); i ++ ){
	        	for(int j = 0; j < D.columns(); j ++ ){
	        		if(i==j){
	        			fw.write(i+"\t"+j+"\t"+D.get(i, j)+"\n");
	        		}
	        		
	        	}
	        	
	        }
	        fw.close();
	        
	        fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/v.txt");
	        for(int i = 0; i  < V.rows(); i ++ ){
	        	for(int j = 0; j < V.columns(); j ++ ){
	        		fw.write(V.get(i, j)+"\t");
	        	}
	        	fw.write("\n");
	        }
	        fw.close();
	        
	        
	        Matrix a = U.multiply(D).multiply(V.transpose());
	        fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/test.txt");
	        for(int i = 0; i < a.rows(); i ++ ){
				for(int j = 0; j < a.columns(); j ++){
					if(a.get(i, j)>1e-10||a.get(i, j)<-1e-10){
						fw.write(i+"\t"+j+"\t"+a.get(i, j)+"\n");
						
					}
				}
				
			}
	        fw.close();
	        
	        /*// trim the matrices according to s
	        Matrix KS = K.getMatrix(0, K.getRowDimension() - 1, 0, s - 1);
	        Matrix SS = S.getMatrix(0, s - 1, 0, s - 1);
	        Matrix DST = D.getMatrix(0, D.getRowDimension() - 1, 0, s - 1).transpose();
	        // check dimensions again
	        System.out.println("KS: " + dim(KS));
	        System.out.println("SS: " + dim(SS));
	        System.out.println("DST: " + dim(DST));*/

	        // print for checking
//	        KS.print(3, 2);
//	        SS.print(3, 2);
//	        DST.print(3, 2);

	    }
	 // returns the dimensions of a matrix
	    private static String dim(Matrix M) {
	        return M.rows() + "x" + M.columns();
	    }


}

