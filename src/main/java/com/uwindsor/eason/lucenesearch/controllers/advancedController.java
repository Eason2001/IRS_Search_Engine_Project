package com.uwindsor.eason.lucenesearch.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class advancedController {

    @RequestMapping("/advanced")
    public String advanced() {
        return "advanced";  //advanced.html has syntax problems
    }


}
