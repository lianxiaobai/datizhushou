package utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BaiDuUtils {
    static CloseableHttpClient httpClient = HttpClients.createDefault();
        public static void main(String[] args) throws IOException {
            String keyWord = "我的人生";
            searchEverything(keyWord);
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


    public static String getHtml(String url) {
        String result = null;
        String text = null;
        try {
            HttpGet get = new HttpGet(url);
            BaiDuUtils.setHeaders(get);
            CloseableHttpResponse res = httpClient.execute(get);
            result = EntityUtils.toString(res.getEntity());
            Document document = Jsoup.parse(result);
            text =  document.getElementById("content_left").text();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String getZixun(String keyword) {
        String result = "";
        try {
             result = getHtml("http://www.baidu.com/s?wd="+URLEncoder.encode(keyword,"UTF-8"));
            /*if (JSON.parseObject(result).getJSONObject("data") != null) {
                zixun = JSON.parseObject(result).getJSONObject("data").getJSONArray("index").getJSONObject(0)
                        .getJSONObject("generalRatio").getString("avg");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
        end = end-start > length ? length:end;
        return question.substring(start,end);

    }


}

