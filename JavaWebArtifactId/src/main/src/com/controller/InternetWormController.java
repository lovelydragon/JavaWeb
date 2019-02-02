package com.controller;

import com.javainternetworm.main.MyCrawler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/worm")
public class InternetWormController {
    @RequestMapping("/top250")
    public ModelAndView top250(){
        System.out.println("开始获取电影");
        ModelAndView modelAndView = new ModelAndView();
        MyCrawler myCrawler = new MyCrawler();
        String url = "https://movie.douban.com/top250?start=0&filter=";
        List<String> names = myCrawler.crawling(new String[]{url});
        modelAndView.addObject("nams",names);
        System.out.println(names);
        return modelAndView;
    }

    @RequestMapping("/getAllMovie")
    public ModelAndView getAllMovie(){
        MyCrawler myCrawler = new MyCrawler();
        myCrawler.crawlingAllMovie();
        return null;
    }
}
