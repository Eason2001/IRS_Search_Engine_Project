package com.uwindsor.eason.lucenesearch.svd_clustering;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

/*import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;*/
import org.la4j.Matrix;
import org.la4j.matrix.DenseMatrix;
//import org.la4j.decomposition.SingularValueDecompositor;
//import org.la4j.matrix.*;
import org.la4j.matrix.dense.Basic2DMatrix;
//import org.la4j.matrix.sparse.CCSMatrix;


public class SVD {
	public static  void main(String[] args) throws IOException{
		
		
		Matrix U=new Basic2DMatrix();
		Matrix Ds=new Basic2DMatrix();
		Matrix V=new Basic2DMatrix();
		Matrix DsV=new Basic2DMatrix();
		Matrix KM=new Basic2DMatrix();
	
		
		
		String pathu = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/u.txt";
		BufferedReader readeru = new BufferedReader(new FileReader(new File(pathu)));
		String lineu;
		int Rowu=0;
		lineu=readeru.readLine();
		String[] t=lineu.split("\t");
		double[][] Umatrix=new double[1653][997];
		
		while((lineu=readeru.readLine())!=null){
			String[] temp = lineu.split("\t");
			for (int j=0; j<temp.length;j++){
				//Matrix U=new Basic2DMatrix(temp.length,temp.length);
				//Umatrix = new double[temp.length][temp.length];
				Umatrix[Rowu][j]=Double.parseDouble(temp[j]);
				//U.set(Rowu,j,u);
			}	
			Rowu++;
		}
		U=Matrix.from2DArray(Umatrix);
		
		
		String pathv = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/v.txt";
		BufferedReader readerv = new BufferedReader(new FileReader(new File(pathv)));
		String linev;
		int Rowv=0;
		linev=readerv.readLine();
		String[] tv=linev.split("\t");
		double[][] Vmatrix=new double[997][997];
		
		while((linev=readerv.readLine())!=null){
			String[] temp = linev.split("\t");
			for (int j=0; j<temp.length;j++){
				Vmatrix[Rowv][j]=Double.parseDouble(temp[j]);
			}
				
			Rowv++;
		}
		
		V=Matrix.from2DArray(Vmatrix);
		
		String pathd = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/d.txt";
		BufferedReader readerd = new BufferedReader(new FileReader(new File(pathd)));
		String lined;
		int Rowd=0;
		double[][] Dmatrix=new double[997][997];

		while((lined=readerd.readLine())!=null){
			String[] temp = lined.split("\t");
			//D.set(Rowd,Rowd,Double.parseDouble(temp[2]));
			
			//Dmatrix[Rowd][j]=Double.parseDouble(temp[2]);
			int Dr=Integer.valueOf(temp[0]);
			int Dc=Integer.valueOf(temp[1]);
			Dmatrix[Dr][Dc]=Double.parseDouble(temp[2]);
			Rowd++;
		}
		
		
		for (int i=0;i<997;i++)
			for (int j=0;j<997;j++)
				if (i!=j) Dmatrix[i][j]= 0.0;
		
		
		/*********************/
		int s=200;
		for (int i=s;i<997;i++)
			for (int j=s;j<997;j++)
				Dmatrix[i][j]= 0.0;
		
		Ds=Matrix.from2DArray(Dmatrix);
		FileWriter fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/ds.txt");
	       for(int i = 0; i  < Ds.rows(); i ++ ){
	       	for(int j = 0; j < Ds.columns(); j ++ ){
	       		fw.write(Ds.get(i, j)+"\t");
	       	}
	       	fw.write("\n");
	       }
	    fw.close();
		
		// print their dimensions for checking
        System.out.println("U: " + dim(U));
        System.out.println("Ds: " + dim(Ds));
        System.out.println("V: " + dim(V));	
        
        DsV=Ds.multiply(V);
        KM=DsV.copyOfRows(s);

        
        // check dimensions again
        /*System.out.println("US: " + dim(US));
        System.out.println("SS: " + dim(SS));*/
        System.out.println("Dsv: " + dim(DsV));

        fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/dsv"+s+".txt");
        for(int i = 0; i  < DsV.rows(); i ++ ){
        	for(int j = 0; j < DsV.columns(); j ++ ){
        		fw.write(DsV.get(i, j)+"\t");
        	}
        	fw.write("\n");
        }
        fw.close();
        
        fw = new FileWriter("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/dsv-norm"+s+".txt");
        double[] col=new double[KM.columns()];//
        col[0]=0.0;
        for (int i=0;i<KM.columns();i++)
        	for(int j=0;j<KM.rows();j++)
        		col[i]+=KM.get(j,i)*KM.get(j,i);
        	
        for (int i=0;i<KM.rows();i++){
        	for(int j=0;j<KM.columns();j++){
        		KM.set(i,j,KM.get(i,j)/Math.sqrt(col[j]));
        		fw.write(KM.get(i, j)+"\t");
        	}
        	fw.write("\n");
        }
	}
	 // returns the dimensions of a matrix
    private static String dim(Matrix M) {
        return  M.rows() + "x" +M.columns();
    }

}
