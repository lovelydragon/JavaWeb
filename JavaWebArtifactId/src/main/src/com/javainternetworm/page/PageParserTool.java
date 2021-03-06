package com.javainternetworm.page;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PageParserTool {


    /* 通过选择器来选取页面的 */
    public static Elements select(Page page , String cssSelector) {
        return page.getDoc().select(cssSelector);
    }

    /*
     *  通过css选择器来得到指定元素;
     *
     *  */
    public static Element select(Page page , String cssSelector, int index) {
        Elements eles = select(page , cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = eles.size() + index;
        }
        return eles.get(realIndex);
    }


    /**
     * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接
     * 例如我们想抽取id为content的div中的所有超链接，这里
     * 就要将cssSelector定义为div[id=content] a
     *  放入set 中 防止重复；
     * @param cssSelector
     * @return
     */
    public static  Set<String> getLinks(Page page ,String cssSelector) {
        Set<String> links  = new HashSet<String>() ;
        Elements es = select(page , cssSelector);
        Iterator iterator  = es.iterator();
        while(iterator.hasNext()) {
            Element element1 = (Element) iterator.next();
            String rank=null;
            for (Element element:element1.children()){
                //加上名次
                String content = element.toString();
                String[] contents = content.split("\n");
                for (String string:contents){
                    if (string.contains("<em class=\"\">")){
                        rank = string.replace("<em class=\"\">","");
                        rank = rank.replace("</em>","");
                    }
                }

                if ( element.hasAttr("href") ) {
                    if (rank == null){
                        links.add(element.attr("abs:href"));
                    }else{
                        links.add(element.attr("abs:href")+" "+rank);
                    }
                }else if( element.hasAttr("src") ){
                    if (rank == null){
                        links.add(element.attr("abs:src")+" "+rank);
                    }else {
                        links.add(element.attr("abs:src")+" "+rank);
                    }

                }
            }
        }
        return links;
    }


//    //不包含子集的链接
//    public static  Set<String> getTop250Links(Page page ,String cssSelector) {
//        Set<String> links  = new HashSet<String>() ;
//        Elements es = select(page , cssSelector);
//        Iterator iterator  = es.iterator();
//        while(iterator.hasNext()) {
//
//            Element element = (Element) iterator.next();
//            if ( element.hasAttr("href") ) {
//                links.add(element.attr("abs:href"));
//            }else if( element.hasAttr("src") ){
//                links.add(element.attr("abs:src"));
//            }
//        }
//        return links;
//    }



    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过getAttrs("img[src]","abs:src")可获取网页中所有图片的链接
     * @param cssSelector
     * @param attrName
     * @return
     */
    public static ArrayList<String> getAttrs(Page page , String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(page ,cssSelector);
        for (Element ele : eles) {
            if (ele.hasAttr(attrName)) {
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }
}
