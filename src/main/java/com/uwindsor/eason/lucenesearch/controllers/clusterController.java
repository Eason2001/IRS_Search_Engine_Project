package com.uwindsor.eason.lucenesearch.controllers;

import com.uwindsor.eason.lucenesearch.svd_clustering.WordKmeans;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class clusterController {

    @RequestMapping("/clustering")
    public String clustering(Model model) throws Exception {

        HashMap<String, float[]> wordMap = new HashMap<String, float[]>();
        //building wordMap;
        InputStreamReader readd = new InputStreamReader(new FileInputStream("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/document.txt"));
        BufferedReader bufferedReaderd = new BufferedReader(readd);
        InputStreamReader readv = new InputStreamReader(new FileInputStream("/Users/Kevin/Documents/InformationRetriveSys/lucenesearch/data/sigmod/SVD-1000/dsv200.txt"));
        BufferedReader bufferedReaderv = new BufferedReader(readv);
        String lineTxtd = null;
        String lineTxtv = null;

        while(((lineTxtd = bufferedReaderd.readLine()) != null)&&((lineTxtv = bufferedReaderv.readLine()) != null)){
            String[] splitsd = lineTxtd.split("\t");
            String[] splitsv = lineTxtv.split("\t");
            System.out.println(splitsd[1]);
            float[] tmpFloat = new float[splitsv.length];
            for (int i = 0; i < splitsv.length; i++)
                tmpFloat[i] = Float.parseFloat(splitsv[i]);
            wordMap.put(splitsd[1], tmpFloat);
        }

        WordKmeans wordKmeans = new WordKmeans(wordMap, 10, 200);
        WordKmeans.Classes[] explain = wordKmeans.explain();
        String clusterStr = "";
        for (int i = 0; i < explain.length; i++) {
            List<Map.Entry<String, Double>> clusterList = new ArrayList<Map.Entry<String, Double>>();
            clusterStr += "-------- Cluster: " + i + " ---------</br>";
            clusterList.addAll(explain[i].getTop(10));
            clusterStr += clusterList.toString() + "</br>";
            clusterList = null;
        }

        model.addAttribute("clusterStr", clusterStr);
        return "clustering";

    }
}
