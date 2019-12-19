package com.ss.wechatpay.pay;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class UrlUtils {

    private static String urll = "http://www.baidu.com/s?wd=";

    /**
     * @Description:
     * @Param: url 抓取地址 num 抓取前几
     * @return:
     * @Author:
     * @Date:
     */
    private static void getNews(String url, int num) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element element = doc.getElementById("content_left");

            for (int i = 1; i < (num + 1); i++) {
                Element result = element.getElementById(String.valueOf(i));
                Elements add = result.select("a");
                System.out.println(add.first().text());
                String attr = add.first().attr("href");
                System.out.println(attr);
                System.out.println(getRealUrlFromBaiduUrl(attr));
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getResult(int num, String question) {
        String url = "";
        try {
            url = urll + URLEncoder.encode(question, "utf-8") + "&rn=" + num;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        getNews(url, num);
    }

    //转成真实url
    public static String getRealUrlFromBaiduUrl(String url) {
        Connection.Response res = null;
        int itimeout = 60000;
        try {
            res = Jsoup.connect(url).timeout(itimeout).method(Connection.Method.GET).followRedirects(false).execute();
            return res.header("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        getResult(50, "健身");
    }
}

