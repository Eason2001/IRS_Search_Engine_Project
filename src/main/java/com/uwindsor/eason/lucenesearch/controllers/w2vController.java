package com.uwindsor.eason.lucenesearch.controllers;

import com.uwindsor.eason.lucenesearch.word2vec.w2vSearch;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class w2vController {

    @RequestMapping("/word2vec")
    public String simWords(@RequestParam(value="keyword") String inputKeyword, Model model) throws Exception {
        if (inputKeyword == "") {
            return "index";
        }

        ArrayList<String> simWords = w2vSearch.simWords(inputKeyword);

        model.addAttribute("simWords", simWords);
        model.addAttribute("ori_keyword", inputKeyword);

        return "word2vec";

    }

}
