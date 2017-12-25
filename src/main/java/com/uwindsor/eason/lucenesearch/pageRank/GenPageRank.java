package com.uwindsor.eason.lucenesearch.pageRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class GenPageRank {

    public static void main(String[] args) throws IOException{
        String path = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/_sigmod_vldb_icse_subgraph.txt";

        FileReader myFileReader=new FileReader(path);
        BufferedReader myBufferedReader=new BufferedReader(myFileReader);
        String line = null;
        ArrayList<String[]> edges= new ArrayList<String[]>();
        while ((line=myBufferedReader.readLine())!=null) {
            String[] pair = line.split("\t");
            edges.add(pair);
        }
        long start = System.currentTimeMillis();
        calc(edges,50);
        long end = System.currentTimeMillis();
        System.out.println(end-start);

    }

    public static void calc(ArrayList<String[]> edges, int itera) throws IOException{
        //String[][] edges = new String[4][2];
        HashSet<String> set = new HashSet<String>();
        for(int in = 0; in < edges.size(); in ++ ){
            set.add(edges.get(in)[0]);
            set.add(edges.get(in)[1]);
        }


        //number o f nodes
        int NodesNum=set.size();
        System.out.println(NodesNum);
        //System.exit(0);

        //dual dimention array, forming a metrix
        double[][] AdjcentMatrix=new double[NodesNum][NodesNum];
        //String[] Nodes=new String[NodesNum];

        ArrayList<String> nodelist = new ArrayList<String>();
        nodelist.addAll(set);

        System.out.println("******************************************"+String.valueOf(NodesNum));
        //myFileReader1.close();
        //initiate adjcent matrix to 0
        for (int m=0;m<NodesNum;m++){
            for (int n=0;n<NodesNum;n++){
                AdjcentMatrix[m][n]=0.0;
            }

        }
        //construct the adjcent matrix
        //FileReader myFileReader2=new FileReader("d:/pagerank/ss.txt");
        //FileReader myFileReader2=new FileReader("d:/pagerank/adjcenttable.txt");
        //BufferedReader myBufferedReader3=new BufferedReader(myFileReader2);
        String myStringNew1=null;
        //while ((myStringNew1=myBufferedReader3.readLine())!=null) {
        for(int i = 0; i < edges.size(); i ++ ){
            String e1=edges.get(i)[0];
            String e2=edges.get(i)[1];
            int RowIndex,ColIndex = 0;
            RowIndex = nodelist.indexOf(e1);
            ColIndex = nodelist.indexOf(e2);
            AdjcentMatrix[RowIndex][ColIndex]=1.0;
        }



        double[] outDegree=new double[NodesNum];
        //deal with the dead end
        for (int m=0;m<NodesNum;m++){
            //to load the first column of AdjcentMatrix
            for (int n=0;n<NodesNum;n++){
                //AdjcentMatrix[m][n]="0";
                outDegree[m]=outDegree[m]+AdjcentMatrix[m][n];

                //System.out.print(AdjcentMatrix[m][n]+" ");
            }
            // if it is a zero vector, set the value to 1/n
            if (outDegree[m]<0.0000001){
                for(int r=0;r<NodesNum;r++){
                    AdjcentMatrix[m][r]=1.0/NodesNum;
                    //System.out.print(AdjcentMatrix[m][r]+" ");
                }
                outDegree[m]=1.0;
            }

            //System.out.println("\r\n");
        }

        double[][] pMatrix=new double[NodesNum][NodesNum];
        for (int m=0;m<NodesNum;m++){
            for (int n=0;n<NodesNum;n++){
                //AdjcentMatrix[m][n]="0";
                double p=0.9*AdjcentMatrix[m][n]/outDegree[m]+0.1/NodesNum;
                pMatrix[m][n]=p;
                //System.out.printf("%7.5f ",p);
            }
            //System.out.println("\r\n");
        }


        // Use the power method to compute page ranks.
        int trials=itera;//iteration times
        double[] rank = new double[NodesNum];
        rank[0] = 1.0;
        for (int t = 0; t < trials; t++) {

            // Compute effect of next move on page ranks.
            System.out.println("round:"+t);
            double[] newRank = new double[NodesNum];
            for (int j = 0; j < NodesNum; j++) {
                //  New rank of page j is dot product of old ranks and column j of p[][].
                for (int k = 0; k < NodesNum; k++)
                    newRank[j] += rank[k]*pMatrix[k][j];
            }

            // Update page ranks.
            rank = newRank;
        }

        // print page ranks
//		       for (int ip = 0; ip < NodesNum; ip++) {
//		    	   System.out.printf("%8.5f", rank[ip]);
//		       }
//		       System.out.println();

        //System.out.println();
        // print page ranks
        FileWriter fw = new FileWriter(new File("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/pagerank_larger.txt"));
        for (int ip = 0; ip < NodesNum; ip++) {
            //System.out.printf("%2d  %5.4f\t", ip, rank[ip]);
            System.out.println(nodelist.get(ip)+"\t"+rank[ip]);
            fw.write(nodelist.get(ip)+"\t"+rank[ip]+"\n");
        }
        fw.close();
        //myFileReader2.close();
        //resultFile.close();


    }

}