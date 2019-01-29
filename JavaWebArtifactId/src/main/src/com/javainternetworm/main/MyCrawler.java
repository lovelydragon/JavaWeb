package com.javainternetworm.main;

import com.javainternetworm.link.LinkFilter;
import com.javainternetworm.link.Links;
import com.javainternetworm.page.Page;
import com.javainternetworm.page.PageParserTool;
import com.javainternetworm.page.RequestAndResponseTool;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyCrawler {


    public static String rootUrl = "https://movie.douban.com/top250?start=0&filter=";
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
                System.out.println(names);
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
        return names;
    }


    //main 方法入口
    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        crawler.crawling(new String[]{rootUrl});
    }
}
