package com.uwindsor.eason.lucenesearch.word2vec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class w2vSearch {
    private static final String ventorPath = "/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/word2vec-master/vectors.bin";

    public static ArrayList<String> simWords(String keywords){
        Word2VEC w_cs = new Word2VEC();
        try {
            w_cs.loadGoogleModel(ventorPath) ;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ArrayList<String> words = new ArrayList<String>();
        Iterator<WordEntry> it = w_cs.distance(keywords).iterator();
        int i = 0;
        while(it.hasNext()){
            words.add(it.next().name);
            if(i++>5){
                break;
            }
        }
        return words;
    }

}
