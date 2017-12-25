package com.uwindsor.eason.lucenesearch.svd_clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class getTfIdf {
	public static  void main(String[] args) throws IOException{
		String indexPath = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod_index";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
	    StandardAnalyzer analyzer = new StandardAnalyzer();
	    analyzer.setVersion(Version.LUCENE_6_3_0);
	    DirectoryReader reader = DirectoryReader.open(dir);
	    float N =4324f+10086f ;
	    String path1 = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/sigmod_id.txt";
	    String path2 = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/sigmod_id.txt";
	    
	    
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
	    
	
	    HashSet<String> words = new HashSet<String>(); 

	    for(int i = 0; i < reader.maxDoc(); i ++ ){
	    	String title = reader.document(i).get("title");
	    	StringTokenizer token = new StringTokenizer(title);
	    	System.out.println("reading title "+i);
	    	while(token.hasMoreTokens()){
	    		words.add(token.nextToken());
	    	}
	    }
	    
	    
		words.removeAll(set);
		
		
		ArrayList<String> vocab = new ArrayList<String>();
		vocab.addAll(words);
		System.out.println(vocab.size());
		float[] idf = new float[vocab.size()];
		for(int i = 0; i < idf.length; i ++ ){
			float df = reader.docFreq(new Term("title",vocab.get(i)));
			System.out.println(vocab.get(i)+"\t"+ df);
			idf[i] = (float) (Math.log10(N/df));
		}
	    
		float[][] table = new float[vocab.size()][reader.maxDoc()];
		for(int i = 0; i < vocab.size(); i ++ ){
			System.out.println("calc the row "+vocab.get(i));
			for(int j = 0; j < reader.maxDoc(); j ++){
				String word = vocab.get(i);
				int tf = 0;
				String[] split = reader.document(j).get("title").split(" ");
				for(int k = 0; k < split.length; k ++ ){
					if(split[k].equals(word)){
						tf++;
					}
				}
				table[i][j] = tf*idf[i];
			}
		}
		
		FileWriter fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/tdTable.txt");
		
		for(int i = 0; i < vocab.size(); i ++ ){
			for(int j = 0; j < reader.maxDoc(); j ++){
				System.out.print(table[i][j]+"\t");
				fw.write(table[i][j]+"\t");
			}
			fw.write("\n");
			System.out.println();
		}
	    
	    
	}

}
