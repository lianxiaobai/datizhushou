package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetUtils {
    public static void main(String[] args) throws IOException {
        /**
         * java代码访问百度
         * 1.创建URL对象
         * 2.创建Http链接
         */
        //URL url = new URL("http://www.baidu.com");
       // URL url = new URL("http://www.baidu.com/s?wd=" + URLEncoder.encode("《倚天屠龙记》中,金毛狮王叫什么名字?","UTF-8"));
        URL url = new URL("http://www.baidu.com/s?wd=《倚天屠龙记》中,金毛狮王叫什么名字?");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        /**
         * 3.设置请求方式
         * 4.设施请求内容类型
         */
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        /**
         * 5.设置请求参数
         * 6.使用输出流发送参数
         */
        //String content="username:";
        //OutputStream outputStream = httpURLConnection.getOutputStream();
        //outputStream.write(content.getBytes());
        /**
         * 7.使用输入流接受数据
         */
        InputStream inputStream = httpURLConnection.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//此处可以用Stringbuffer等接收
        byte[] b = new byte[1024];
        int len=0;
        while (true) {
            len=inputStream.read(b);
            if (len == -1) {
                break;
            }
            byteArrayOutputStream.write(b,0,len);
        }
        System.out.println(byteArrayOutputStream.toString());
    }
}
