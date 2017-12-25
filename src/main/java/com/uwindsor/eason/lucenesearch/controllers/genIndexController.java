package com.uwindsor.eason.lucenesearch.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.uwindsor.eason.lucenesearch.indexObjects.IndexAllFilesInDirectory;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAutoConfiguration
public class genIndexController {

    @RequestMapping("/genIndex")
    public String retIndexPage() {

        return "genIndex";
    }

    @RequestMapping(value = "/genIndex", method = RequestMethod.POST)
    public String genIndex(@RequestParam(value="docsPath") String docsPath, @RequestParam(value="indexPath") String indexPath ) throws Exception {
        IndexAllFilesInDirectory genIDX = new IndexAllFilesInDirectory(indexPath , docsPath);
        genIDX.IndexAllFiles();

        return "genSuccess";
    }

}
