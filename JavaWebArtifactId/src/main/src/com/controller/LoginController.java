package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "login")
public class LoginController {

    @RequestMapping(value = "test")
    public String test(){
        System.out.println("test");
        return "index";
    }
}
