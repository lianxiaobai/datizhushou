package pattern.impl;

import com.baidu.aip.nlp.AipNlp;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ocr.Ocr;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import pattern.Pattern;
import search.impl.SearchFactory;
import similarity.Similarity;
import similarity.impl.BaiDuSimilarity;
import similarity.impl.SimilarityFactory;
import utils.*;
import pojo.Information;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Created by lingfengsan on 2018/1/18.
 *
 * @author lingfengsan
 */
public class CommonPattern implements Pattern {
    private static final String QUESTION_FLAG = "?";
    private static int[] startX = {100, 100, 80};
    private static int[] startY = {300, 300, 300};
    private static int[] width = {900, 900, 900};
    private static int[] height = {900, 900, 700};
    private ImageHelper imageHelper = new ImageHelper();
    private SearchFactory searchFactory = new SearchFactory();
    private int patterSelection;
    private int searchSelection;
    private Ocr ocr;
    private Utils utils;
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("search").build();

    private ExecutorService pools = new ThreadPoolExecutor(8, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    public void setPatterSelection(int patterSelection) {
        this.patterSelection = patterSelection;
    }

    public void setSearchSelection(int searchSelection) {
        this.searchSelection = searchSelection;
    }

    public void setOcr(Ocr ocr) {
        this.ocr = ocr;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }


    @Override
    public String run() throws UnsupportedEncodingException {
        //       记录开始时间
        long startTime;
        //       记录结束时间
        long endTime;
        StringBuilder sb = new StringBuilder();
        startTime = System.currentTimeMillis();
        //获取图片
        String imagePath = utils.getImage();
        System.out.println("图片获取成功");
        //裁剪图片
        //imageHelper.cutImage(imagePath, imagePath,
                //startX[patterSelection], startY[patterSelection], width[patterSelection], height[patterSelection]);
        imageHelper.cutImage(imagePath, imagePath,
                100, 450, width[patterSelection], 1000);

        //图像识别
        Long beginOfDetect = System.currentTimeMillis();
        String questionAndAnswers = ocr.getOCR(new File(imagePath));
        sb.append(questionAndAnswers);
/*
        int local = questionAndAnswers.indexOf('?');
        String tempquestion = questionAndAnswers.substring(questionAndAnswers.indexOf('.')+1,local);
        //1.打开浏览器
      //HttpClientUtils.httpGet("http://www.baidu.com/s?wd=" + URLEncoder.encode(tempquestion,"UTF-8"));
       */
/* HttpGet get =  new HttpGet("http://www.baidu.com/s?wd="+ URLEncoder.encode(tempquestion,"UTF-8"));
        BaiDuUtils.setHeaders(get);
        System.out.println();*//*

        String temresult = BaiDuUtils.getZixun(tempquestion);

        String[] tempanswers  = questionAndAnswers.substring(local+2).split("\n");

*/




        System.out.println("识别成功");
        System.out.println("识别时间：" + (System.currentTimeMillis() - beginOfDetect));
     /*   if (questionAndAnswers == null || !questionAndAnswers.contains(QUESTION_FLAG)) {
            sb.append("问题识别失败，输入回车继续运行\n");
            return sb.toString();
        }*/
        //获取问题和答案
        System.out.println("检测到题目");
        Information information = utils.getInformation(questionAndAnswers,0);
        String question = information.getQuestion();
        String[] answers = information.getAns();
      /*  if (question == null) {
            sb.append("问题不存在，继续运行\n");
            return sb.toString();
        } else if (answers.length < 1) {
            sb.append("检测不到答案，继续运行\n");
            return sb.toString();
        }*/
        //sb.append("问题:").append(question).append("\n");
        //sb.append("答案：\n");
        FutureTask<String> futurehtml = new FutureTask<String>(() -> {
                return BaiDuUtils.getZixun(question);
        });
        pools.execute(futurehtml);
        FutureTask<String> futurehtml2 = new FutureTask<String>(() -> {
            return BaiDuUtils.getZixun(StringUtils.join(answers) + BaiDuUtils.questionHandler(question));
        });
        pools.execute(futurehtml2);


        //求相关性
        int numOfAnswer = answers.length;
        int[] result = new int[numOfAnswer];
        int[] result2 = new int[numOfAnswer];
        Similarity[] similarities = new Similarity[numOfAnswer];
        Similarity[] similarities2 = new Similarity[numOfAnswer];
        FutureTask[] futureTasks = new FutureTask[numOfAnswer];
        FutureTask[] futureTasks2 = new FutureTask[numOfAnswer];

        String html = "";
        try {
            html = futurehtml.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
     /*   String ans = "";
        for (String s: answers) {
            ans += s + " ";
        }*/

        String html2 = "";
        try {
            html2 = futurehtml2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //CountDownLatch countDownLatch = new CountDownLatch(5);
      /*  int[] MatchCount = new int[4];
        int idx = 0;
        for (final String answer : answers) {
            sb.append(answer).append("\n");
           *//* executor.execute(()->{
                MatchCount[idx] = QuickMatch.Sunday(html,answer);
            });
            idx++;*//*
            int matchcount = QuickMatch.Sunday(html,answer);
            idx++;

        }*/
        //for (final String answer : answers) {
           // sb.append(answer + "｛" + QuickMatch.Sunday(html,answer) + " }").append("\n");
        //    sb.append(answer).append("\n");
        //}


         //String htmlMat = BaiDuUtils.getZixun(question);

      /*  BaiDuSimilarity.setClient(new AipNlp("10732092", "pdAtmzlooEbrcfYG4l0kIluf",
                "sHjPBnKt58crPuFogTgQ5Wki0TrHYO2c"));*/
        for (int i = 0; i < numOfAnswer; i++) {
            similarities[i] = SimilarityFactory.getSimlarity(2, html, answers[i]);
            similarities2[i] = SimilarityFactory.getSimlarity(3, html2, answers[i]);
            futureTasks[i] = new FutureTask<Integer>(similarities[i]);
            futureTasks2[i] = new FutureTask<Integer>(similarities2[i]);
            pools.execute(futureTasks[i]);
            pools.execute(futureTasks2[i]);
        }
        for (int i = 0; i < numOfAnswer; i++) {
            while (true) {
                if (futureTasks[i].isDone()) {
                    break;
                }
            }
            try {
                result[i] = (int)futureTasks[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < numOfAnswer; i++) {
            while (true) {
                if (futureTasks2[i].isDone()) {
                    break;
                }
            }
            try {
                result2[i] = (int)futureTasks2[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //搜索

        /*FutureTask[] futureQuestion = new FutureTask[1];
        futureQuestion[0] = new FutureTask<Long>(searchFactory.getSearch(searchSelection, question, true));
        pools.execute(futureQuestion[0]);*/


        //根据pmi值进行打印搜索结果
        //int[] rank = Utils.rank(result);
     /*   for (int i=0 ; i < answers.length;i++) {
            sb.append(answers[i]);
            sb.append(" 相似度为:").append(result[i]).append("\n");
        }*/
/*
        sb.append("--------最终结果-------\n");
        sb.append(answers[rank[rank.length - 1]]);*/
        sb.append("--------最终结果-------\n");
        for(int i = 0; i < answers.length; i++){
            sb.append(answers[i] + ": " + result[i] + "  ------  " + result2[i]).append("\n");

        }
        endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;

        sb.append("执行时间：").append(excTime).append("s").append("\n");
        return sb.toString();
    }
}
