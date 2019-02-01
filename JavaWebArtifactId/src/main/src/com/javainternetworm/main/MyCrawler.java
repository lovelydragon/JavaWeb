package com.javainternetworm.main;

import com.bean.Movie;
import com.javainternetworm.link.LinkFilter;
import com.javainternetworm.link.Links;
import com.javainternetworm.page.Page;
import com.javainternetworm.page.PageParserTool;
import com.javainternetworm.page.RequestAndResponseTool;
import com.javainternetworm.util.PrintExcel;
import com.sun.deploy.util.StringUtils;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCrawler {


    public static String rootUrl = "https://movie.douban.com/top250?start=0&filter=";
//    public static String rootUrl = "http://127.0.0.1:8080/JavaWebArtifactId_war/index.jsp";
    /**
     * 使用种子初始化 URL 队列
     *
     * @param seeds 种子 URL
     * @return
     */
    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++){
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }


    public List<Movie> crawlingTop250(String url){
        List<Movie> movies = new ArrayList<>();

        initCrawlerWithSeeds(new String[]{url});

        //定义过滤器，提取以 rootUrl 开头的链接
        LinkFilter filter = new LinkFilter() {
            public boolean accept(String url) {
                if (url.startsWith(url))
                    return true;
                else
                    return false;
            }
        };

        //获取所有top250的链接
        String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
        Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);
        Links.addVisitedUrlSet(visitUrl);
        Set<String> links = PageParserTool.getLinks(page,".paginator");
        for (String link : links) {
            Links.addUnvisitedUrlQueue(link);
        }
        Set<String> movieLinks = new HashSet<>();
        Set<String> movieTempLinks = new HashSet<>();
        movieTempLinks = PageParserTool.getLinks(page,".pic");
        for (String link : movieTempLinks) {
//            Links.addUnvisitedUrlQueue(link);
            movieLinks.add(link);
        }
        while (!Links.unVisitedUrlQueueIsEmpty()) {
            visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null) {
                continue;
            }
            page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);
            movieTempLinks = PageParserTool.getLinks(page, ".pic");
            for (String link : movieTempLinks) {
                movieLinks.add(link);
            }
            Links.addVisitedUrlSet(visitUrl);
        }
        for (String str:movieLinks){
            Links.addUnvisitedUrlQueue(str);
        }
        //获取所需要的所有信息
        Movie movie;
        List<String> temp = new ArrayList<>();
//        while(!Links.unVisitedUrlQueueIsEmpty()){
            movie = new Movie();
            visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null) {
//                continue;
            }
            String[] visitUrls = visitUrl.split(" ");
            if (visitUrls.length>1){
                movie.setRank(visitUrls[1]);
            }
            page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrls[0]);
            Elements es = PageParserTool.select(page,".subjectwrap");
            Elements names = PageParserTool.select(page,"span[property=v:itemreviewed]");
            String namesStr = names.toString();
            String name = ">(.*?)</span>";
            String director = "rel=\"v:directedBy\">(.*?)</a>";
//            String country = ":</span> (.*?)";
            String screenWriter = "/\">(.*?)</a>";
            String year = "content=\"(.*?)\">";
            String score = "v:average\">(.*?)</strong>";
            String peopleNum = "v:votes\">(.*?)</span>";
            int i = 0;
            if(!es.isEmpty()){
                System.out.println("下面将打印所有符合要求标签内容： ");
                String contents = es.toString();
                String[] content = contents.split("\n");
                for (String string:content){
                    if (string.contains("制片国家/地区:")){
                        movie.setCountry(string.replace("<span class=\"pl\">制片国家/地区:</span> ",""));
                        break;
                    }
                }
                temp = getRet(namesStr,name);
                movie.setName(temp.toString());
                temp = getRet(contents,director);
                movie.setDirector(StringUtils.join(temp,"/"));
//                temp = getRet(contents,country);
//                movie.setCountry(StringUtils.join(temp,"/"));
                temp = getRet(contents,screenWriter);
                movie.setScreenWriter(StringUtils.join(temp,"/"));
                temp = getRet(contents,year);
                movie.setDuration(temp.get(temp.size()-2));
                temp.remove(temp.size()-1);
                temp.remove(temp.size()-1);
                movie.setYear(StringUtils.join(temp,"/"));
                temp = getRet(contents,score);
                movie.setScore(temp.get(temp.size()-1));
                temp = getRet(contents,peopleNum);
                movie.setPeopleNum(StringUtils.join(temp,"/"));
            }
            Links.addVisitedUrlSet(visitUrl);
            movies.add(movie);
//        }
        return movies;
    }

    public List<String> getRet(String contents,String pat){
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(contents);
        List<String> ret = new ArrayList<>();
        while (matcher.find()){
            int i = 1;
            System.out.println("find:"+matcher.group(i));
            ret.add(matcher.group(i));
            i++;
        }
        return ret;
    }


    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public List<String> crawling(final String[] seeds) {
        List<String> names = new ArrayList<String>();

        //初始化 URL 队列
        initCrawlerWithSeeds(seeds);

        //定义过滤器，提取以 rootUrl 开头的链接
        LinkFilter filter = new LinkFilter() {
            public boolean accept(String url) {
                if (url.startsWith(seeds[0]))
                    return true;
                else
                    return false;
            }
        };

        //循环条件：待抓取的链接不空且抓取的网页不多于 1000
        while (!Links.unVisitedUrlQueueIsEmpty()  && Links.getVisitedUrlNum() <= 1000) {

            //先从待访问的序列中取出第一个；
            String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,".title");
            if(!es.isEmpty()){
                System.out.println("下面将打印所有符合要求标签内容： ");
                String content = es.outerHtml();
                String[] contents = content.split("\n");
                for (String str : contents){
                    if (str.contains("&nbsp;")){
                        continue;
                    }
                    str = str.replace("<span class=\"title\">","");
                    str = str.replace("</span>","");
                    names.add(str);
                }
            }

            //将保存文件
//            FileTool.saveToLocal(page);

            //将已经访问过的链接放入已访问的链接中；
            Links.addVisitedUrlSet(visitUrl);

            //得到超链接
            Set<String> links = PageParserTool.getLinks(page,".paginator");
            for (String link : links) {
                Links.addUnvisitedUrlQueue(link);
//                System.out.println("新增爬取路径: " + link);
            }
        }
        System.out.println(names);
        return names;
    }


    //main 方法入口
    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
//        crawler.crawling(new String[]{rootUrl});
        List<Movie> movies = new ArrayList<>();
        movies = crawler.crawlingTop250(rootUrl);

        PrintExcel printExcel = new PrintExcel();
        printExcel.createTop250();
        printExcel.printTop250(movies);
    }
}
