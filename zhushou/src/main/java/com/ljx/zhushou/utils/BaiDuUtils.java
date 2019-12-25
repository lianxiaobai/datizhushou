package com.ljx.zhushou.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljx.zhushou.bean.HtmlText;
import com.ljx.zhushou.bean.TextString;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class BaiDuUtils {
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static Base64.Decoder decoder = Base64.getDecoder();
        public static void main(String[] args) throws IOException {
            //textString.setQuestion("12. 俗语“有眼不识泰山”最早记载于哪本名著？");
            //textString.setAnswers(new String[]{"西游记","水浒传","三国演义","红楼梦"});
            //String keyWord = "西游记 ";
            //getPromptWords(keyWord,"12. 俗语“有眼不识泰山”最早记载于哪本名著？");
            getZixun("欧阳修曾以图中建筑为题写下了千古名篇，下列哪个成语出自于该篇文章？");
        }

        public static void searchEverything(String keyWord) throws IOException {
            URL url = new URL("https://www.baidu.com/s?wd="+keyWord);//搜索功能在二级域名中
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            // httpURLConnection.setRequestProperty();
            /**
             * 接收数据
             */
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while (true) {
                len = inputStream.read(b);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(b, 0, len);
            }
            System.out.println(byteArrayOutputStream.toString());
        }
     public static void setSoGouHeaders(HttpGet get) {
        /*
        * GET /api/anspush?key=bwyx&wdcallback=jQuery200040074935650220844_1576642001794&_=1576642001795 HTTP/1.1
Host: wzpush.sogoucdn.com
Connection: keep-alive
User-Agent: Mozilla/5.0 (Linux; Android 6.0; CAM-TL00H Build/HONORCAM-TL00H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 SGInfo/720/1208/2.0 SogouSearch Android1.0 version3.0 AppVersion/7351

        Referer: https://wzassistant.sogoucdn.com/v5/cheat-sheet?channel=bwyx&icon=http%3A%2F%2Fapp.sastatic.sogoucdn.com%2Fpic%2Fthyx2.png&name=%E5%A4%B4%E5%8F%B7%E8%8B%B1%E9%9B%84&appName=undefined
        Accept-Encoding: gzip, deflate
        Accept-Language: zh-CN,en-US;q=0.8
        Cookie: APP-SGS-ID=ab26860863030234538
        X-Requested-With: com.sogou.activity.src

                * */
        get.setHeader("Accept",
                "*/*");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-CN,en-US;q=0.8");
        get.setHeader("User-Agent",
                "Mozilla/5.0 (Linux; Android 6.0; CAM-TL00H Build/HONORCAM-TL00H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 SGInfo/720/1208/2.0 SogouSearch Android1.0 version3.0 AppVersion/7351");
        get.setHeader("X-Requested-With", "com.sogou.activity.src");
        get.setHeader("Cookie", "APP-SGS-ID=ab26860863030234538");//自己登录的cookie信息
        get.setHeader("Host", "wzpush.sogoucdn.com");
        get.setHeader("Referer","https://wzassistant.sogoucdn.com/v5/cheat-sheet?channel=bwyx&icon=http%3A%2F%2Fapp.sastatic.sogoucdn.com%2Fpic%2Fthyx2.png&name=%E5%A4%B4%E5%8F%B7%E8%8B%B1%E9%9B%84&appName=undefined");
        get.setHeader("Connection", "keep-alive");

        }
    /*
      访问百度搜索请求头
     */
    public static void setHeaders(HttpGet get) {
        get.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        get.setHeader("Accept-Encoding", "gzip, deflate, br");
        get.setHeader("Cache-Control", "max-age=0");

        get.setHeader("Connection", "keep-alive");
        get.setHeader("Cookie", "BIDUPSID=35859F327EC41753A23E5FE005123D4B; PSTM=1526094490; BAIDUID=4F1A2ADB72ED0C351F4AB1B3B543CD70:FG=1; MCITY=-257%3A; BD_UPN=12314753; H_PS_PSSID=1437_21107_30211_26350; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BDSFRCVID=Yt4sJeCCxG3e3OrwnTNBsTNkb0nC19fA_OA_3J; BDUSS=VNYjVCb3BFM0Z2UmdKbnRZSUtsRFRlWWdpSzg5LVZxaHdTelprQnBBSGRwQjVlSUFBQUFBJCQAAAAAAAAAAAEAAADfjkJituB4ZGy148rCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAN0X913dF~ddWj; delPer=0; BD_CK_SAM=1; PSINO=6; ZD_ENTRY=baidu; sug=3; sugstore=0; ORIGIN=2; bdime=0; H_PS_645EC=6aecmOAsrjmg0cn2nk2tIta8bvYoIsGs3DbL1crH4A5xqA1csEgVIdSSyAs");//自己登录的cookie信息
        get.setHeader("Host", "www.baidu.com");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("Sec-Fetch-Mode", "navigate");
        get.setHeader("Sec-Fetch-Site", "none");
        get.setHeader("  Upgrade-Insecure-Requests", "1");
        get.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
    }
    public static TextString getText(String url) {
        TextString textString = new TextString();
        String result = null;
        String text = null;
        try {
            HttpGet get = new HttpGet(url);
            BaiDuUtils.setSoGouHeaders(get);
            CloseableHttpResponse res = httpClient.execute(get);
            result = EntityUtils.toString(res.getEntity());
            result = result.substring(result.indexOf("{"),result.indexOf("}")+1);
            String str = new String(decoder.decode(JSON.parseObject(result).get("result").toString()), "UTF-8");
            JSONObject  jsonObject = JSON.parseObject(JSON.parseArray(str).get(1).toString());//.get("answers").toString();//get("answers");
            String title = jsonObject.get("title").toString();
            JSONArray jsonArray = JSON.parseArray(jsonObject.get("answers").toString());
            textString.setAnswers(jsonArray.toArray(new String[jsonArray.size()]));
            textString.setQuestion(title);
            textString.setSoGouAnswer(jsonObject.get("result").toString());
            //System.out.println(new String(decoder.decode(str), "UTF-8"));
            //Document document = Jsoup.parse(result);
            //text =  document.getElementById("content_left").text();
            textString.setQuestion("12.欧阳修会，下列哪个成语出自于该篇文章？");
            textString.setAnswers(new String[]{"心旷神怡","无人问津","水落石出"});
            return textString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textString;
    }

    public static HtmlText getZixun(String keyword) {
        String result = "";
        HtmlText htmlText = new HtmlText();
        try {
            htmlText = getHtml("http://www.baidu.com/s?wd="+URLEncoder.encode(keyword,"UTF-8"));
            /*if (JSON.parseObject(result).getJSONObject("data") != null) {
                zixun = JSON.parseObject(result).getJSONObject("data").getJSONArray("index").getJSONObject(0)
                        .getJSONObject("generalRatio").getString("avg");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("keyword:" + keyword);
        //System.out.println("result: " + result);
        return htmlText;
    }
    /*
     * 获取百度百科搜索结果
     */
    public static String getBaiKeHtml(String keyword) {
        String result = "";
        try {
            String Url = "https://baike.baidu.com/search/word?word="+URLEncoder.encode(keyword,"UTF-8");
            CloseableHttpResponse response = httpBaiduBaiKe(Url);
            String respContent = EntityUtils.toString(response.getEntity() , "UTF-8").trim();
            //result = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(respContent);
            String text = document.getElementsByClass("content").text();

            //String stext="";
            //result = elements.get(0).text();
            /*if (JSON.parseObject(result).getJSONObject("data") != null) {
                zixun = JSON.parseObject(result).getJSONObject("data").getJSONArray("index").getJSONObject(0)
                        .getJSONObject("generalRatio").getString("avg");
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("keyword:" + keyword);
        //System.out.println("result: " + result);
        return result;
    }
    /*
      获取百度搜索的结果
     */
    public static HtmlText getHtml(String url) {
        String result = null;
        HtmlText htmlText = new HtmlText();
        String text = "";
        try {
            CloseableHttpResponse res = httpBaidu(url);
            result = EntityUtils.toString(res.getEntity());
            Document document = Jsoup.parse(result);
            //ArrayList element = document.getElementsByTag("em").stream().distinct().collect(Collectors.toCollection(ArrayList::new));
            List<String> elements = document.getElementsByTag("em").stream().map(element -> element.text()).distinct().collect(Collectors.toList());
            //elements.text()
            text =  document.getElementById("content_left").text();
           System.out.println("url" + url);
            //System.out.println("result:" + result);
            //System.out.println("text:" + text);
            htmlText.setHtml(text);
            htmlText.setList(elements);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return htmlText;
        }

    }
    public static CloseableHttpResponse httpBaidu(String url){
        HttpGet get = new HttpGet(url);
        BaiDuUtils.setHeaders(get);
        CloseableHttpResponse res = null;
        try {
            //get.addHeader("Content-type","application/json; charset=gbk");
            res = httpClient.execute(get);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static CloseableHttpResponse httpBaiduBaiKe(String url){
        HttpGet get = new HttpGet(url);
        BaiDuUtils.setBaiKeHeaders(get);
        CloseableHttpResponse res = null;
        try {
            //get.addHeader("Content-type","application/json; charset=gbk");
            res = httpClient.execute(get);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String questionHandler(String question){
        int i = question.indexOf("的")+1;
        int length = question.length();
        int start = 6;
        int end = 26;
        if(i>0){
             start = i-9>0?i-9:0;
             end = i+11;
        }
        end = end > length-2 ? length-2:end;
        String temp = question.substring(start,end);
        //System.out.println("temp:" + temp);
        return temp;

    }

    public static int getPromptWords(String keyword,String question) {
        TextString textString = new TextString();
        String result = null;
        //统计
        int count=0;
        try {
            HttpGet get = new HttpGet("https://www.baidu.com/sugrec?pre=1&p=3&ie=utf-8&json=1&prod=pc&from=pc_web&sugsid=1437,21107,30211,30388,30284,26350&csor=1&cb=jQuery110204117535545587623_1577091530166&_=1577091530167&wd="+URLEncoder.encode(keyword,"UTF-8"));
            BaiDuUtils.setHeaders(get);
            CloseableHttpResponse res = httpClient.execute(get);
            result = EntityUtils.toString(res.getEntity());
            result = result.substring(result.indexOf("(")+1,result.indexOf(")"));
            //String str = new String(decoder.decode(JSON.parseObject(result).get("result").toString()), "UTF-8");
            String  str  = JSON.parseObject(result).get("g").toString();//.get("answers").toString();//get("answers");
            //String title = jsonObject.get("title").toString();
            JSONArray jsonArray = JSON.parseArray(str);
            for (int i = 0; i < jsonArray.size(); i++) {
                if(QuickMatch.Sunday(question,jsonArray.getJSONObject(i).getString("q").toString().split(" ")[1])>0){
                    count++;
                }
            }

           // textString.setQuestion("12. 俗语“有眼不识泰山”最早记载于哪本名著？");
            //textString.setAnswers(new String[]{"西游记","水浒传","三国演义","红楼梦"});
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;

    }

    public static void setBaiKeHeaders(HttpGet get){
        get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        get.setHeader("Accept-Encoding"," gzip, deflate, br");
        get.setHeader("Accept-Language","ia,zh-CN;q=0.9,zh;q=0.8");
        get.setHeader("Connection","keep-alive");
        get.setHeader("Cookie","BIDUPSID=35859F327EC41753A23E5FE005123D4B; PSTM=1526094490; BAIDUID=4F1A2ADB72ED0C351F4AB1B3B543CD70:FG=1; MCITY=-257%3A; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BDUSS=0Y4aFRZY215bTJoWTF5eExFbH5PbTRGR3h-SWpaT1NYeVo3VEZBSkdZaEdNU0ZlSVFBQUFBJCQAAAAAAAAAAAEAAADfjkJituB4ZGy148rCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEak-V1GpPldT; __cfduid=d61b02a318747650c293cca700eb81b181576672819; baikedeclare=showed; H_PS_PSSID=1437_21107_30211_30284_26350; Hm_lvt_55b574651fcae74b0a9f1cf9c8d7c93a=1577177445,1577177660,1577178659,1577182148; delPer=0; PSINO=6; pgv_pvi=4901133312; pgv_si=s7667282944; BK_SEARCHLOG=%7B%22key%22%3A%5B%22%E6%B0%B4%E8%90%BD%E7%9F%B3%E5%87%BA%22%2C%22%E7%94%9F%E7%89%A9%E7%BB%92%22%5D%7D; Hm_lpvt_55b574651fcae74b0a9f1cf9c8d7c93a=1577188562");
        get.setHeader("Host","baike.baidu.com");
        get.setHeader("Referer","https://baike.baidu.com/item/%E6%B0%B4%E8%90%BD%E7%9F%B3%E5%87%BA4/2248299?fromtitle=%E6%B0%B4%E8%90%BD%E7%9F%B3%E5%87%BA&fromid=8558497");
        get.setHeader("Sec-Fetch-Mode","navigate");
        get.setHeader("Sec-Fetch-Site","same-origin");
        get.setHeader("Sec-Fetch-User","?1");
        get.setHeader("Upgrade-Insecure-Requests","1");
        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
    }



}

