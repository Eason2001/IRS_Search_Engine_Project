package com.uwindsor.eason.lucenesearch.controllers;

import com.uwindsor.eason.lucenesearch.indexObjects.Article;
import com.uwindsor.eason.lucenesearch.word2vec.w2vSearch;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uwindsor.eason.lucenesearch.indexObjects.SearchIndexedDocs;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
@EnableAutoConfiguration
public class resultController {

    private  static final int pageSize = 10;
    private static String docsPath = "/Users/Kevin/Documents/AdvanceComputeConcept/Project/SearchEngine/data/MitNews_txt/";
    @RequestMapping("/result")
    public String result(@RequestParam(value="queryString") String inputString, @RequestParam(value="pageIndex") int inputPageIndex, Model model) throws Exception {
        if (inputString == "") {
            return "index";
        }
        //break down the inputString into String[]
        List<String> queryString = new ArrayList<String>();
        String[] tempStr= inputString.split("\\s{1,}");
        Collections.addAll(queryString, tempStr);
        SearchIndexedDocs searIDX = new SearchIndexedDocs("/data/sigmod_index");
        long start = System.currentTimeMillis();
        //inputPageIndex start from 0, need to plus 1 
        List<Article> articlelist = searIDX.Search(queryString, inputPageIndex+1, pageSize);
        long end = System.currentTimeMillis();

        int totalHits;
        if (articlelist == null) {
            totalHits = 0;
        } else {
            totalHits = articlelist.get(0).getTotalhits();
        }

        ArrayList<String> simWords = w2vSearch.simWords(tempStr[0]);

        model.addAttribute("simWords", simWords);
        model.addAttribute("articlelist", articlelist);
        model.addAttribute("totalHits", totalHits);
        model.addAttribute("timeSpent", end - start);
        model.addAttribute("ori_queryString", inputString);
        model.addAttribute("ori_pageIndex", inputPageIndex);
        model.addAttribute("pageSize", pageSize);
        return "result";

    }

    @RequestMapping("/articledetail")
    public String getarticledetail(@RequestParam(value="docId") String inputDocId, Model model) throws Exception {

        if (inputDocId == "") {
            return "index";
        }

        Article article = new Article();
        SearchIndexedDocs searIDX = new SearchIndexedDocs("/data/sigmod_index");
        article = searIDX.SearchDocId(inputDocId);
        model.addAttribute("article", article);

        return "articledetail";

    }



}
