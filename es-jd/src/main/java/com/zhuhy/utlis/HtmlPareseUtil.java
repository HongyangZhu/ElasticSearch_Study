package com.zhuhy.utlis;

import com.zhuhy.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlPareseUtil {
//    public static void main(String[] args) throws IOException {
//        new HtmlPareseUtil().parseJD("vue").forEach(System.out::println);
//
//    }
    public List<Content> parseJD(String keywords) throws IOException {
        // 获取请求 https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        // 解析网页(Jsoup返回Document就是浏览器的DOM)
        Document document = Jsoup.parse(new URL(url), 30000);
        // 所有在JS中使用的方法都可以使用
        Element element = document.getElementById("J_goodsList");
//        System.out.println(element.html());

        // 获取所有的li元素
        Elements elements = element.getElementsByTag("li");
        ArrayList<Content> goodsList = new ArrayList<>();
        // 获取元素中的内容
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

//            System.out.println("===========================");
//            System.out.println(img);
//            System.out.println(price);
//            System.out.println(title);
            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            goodsList.add(content);
        }

        return goodsList;
    }
}
