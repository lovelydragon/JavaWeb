package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/test")
    public String test(){
//        ModelAndView modelAndView = new ModelAndView();
        System.out.println("test");
        return "index";
    }

    @RequestMapping(value = "modelAndView")
    public ModelAndView modelAndView(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
