package com.uwindsor.eason.lucenesearch.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@EnableAutoConfiguration
public class indexController {

    @RequestMapping("/")
    String index() {
        return "index";
    }


}



