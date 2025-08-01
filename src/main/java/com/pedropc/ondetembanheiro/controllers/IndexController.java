package com.pedropc.ondetembanheiro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @GetMapping("/sobre")
    public String about() {
        return "sobre";
    }
}
