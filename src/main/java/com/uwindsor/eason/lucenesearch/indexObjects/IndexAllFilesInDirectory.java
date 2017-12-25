package com.uwindsor.eason.lucenesearch.indexObjects;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Index all text files under a directory.
 */
public class IndexAllFilesInDirectory {
	private int counter = 0;
//	public static String prefixPath = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch";
    private String indexPath = "";
    private String docsPath = "";

	public IndexAllFilesInDirectory(String indexPath, String docsPath) {
        this.indexPath = indexPath;
        this.docsPath = docsPath;
        this.counter = 0;

	}

	public void IndexAllFiles() throws Exception {

		System.out.println("Indexing to directory '" + indexPath + "'...");
		Directory dir = FSDirectory.open(Paths.get(indexPath ));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = new IndexWriter(dir, iwc);


//		preparing for pageRank:
        InputStreamReader read = new InputStreamReader(new FileInputStream(docsPath + "/pagerank_larger.txt"));
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;

        HashMap<String,String> pagerank = new HashMap<String,String>();
        while((lineTxt = bufferedReader.readLine()) != null){
            String[] splits = lineTxt.split("\\s");
            System.out.println(splits[0]);
            pagerank.put(splits[0], splits[1]);
        }
        System.out.println(pagerank.size());


        FieldType ft = new FieldType();
        // ft.setDocValuesType(DocValuesType.SORTED_NUMERIC);
        ft.setStored(true);
        ft.setStoreTermVectors(true);
        ft.setStoreTermVectorOffsets(true);
        ft.setStoreTermVectorPositions(true);
        ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);

        read = new InputStreamReader(new FileInputStream(docsPath + "/sigmod_id.txt"));
        bufferedReader = new BufferedReader(read);
        lineTxt = null;

        //int i =0;
        while ((lineTxt = bufferedReader.readLine()) != null)
        {
            //text +=lineTxt+"\n";
            Document doc = new Document();
            //System.out.println(lineTxt);
            String[] split = lineTxt.split("\t");
            long docId = Long.parseLong(split[0],16);
            String title = split[1];
            String year = split[3];
            String publisher = split[7];
            System.out.println(docId);
            doc.add(new Field("docId",docId+"",TextField.TYPE_STORED));
            doc.add(new Field("year",year,TextField.TYPE_STORED));

            Field titleF = new Field("title",title,ft);
            titleF.setBoost((float) (pagerank.get(docId+"")==null?1.0:Float.parseFloat(pagerank.get(docId+""))*10E5));
            System.out.println((float) (pagerank.get(docId+"")==null?1.0:Float.parseFloat(pagerank.get(docId+""))*10E5));
            doc.add(titleF);
            doc.add(new Field("publisher",publisher,ft));
            //System.out.println("indexing:"+id+"\t"+edges1.get(id+""));
            writer.addDocument(doc);
        }
        writer.forceMerge(1);
		writer.close();
        dir.close();

	}


}