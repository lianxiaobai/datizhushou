package com.ljx.zhushou.Handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljx.zhushou.bean.HtmlText;
import com.ljx.zhushou.utils.BaiDuUtils;
import com.ljx.zhushou.utils.QuickMatch;
import com.ljx.zhushou.utils.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class VisitBaiDuHandler {
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("search").build();
    private ExecutorService pools = new ThreadPoolExecutor(8, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    public VisitBaiDuHandler(ExecutorService pools){
        this.pools = pools;
    }
    /*
     * 创建线程去百度搜索
     */
    public FutureTask<HtmlText>  getSearchHtml(String keyword){
        FutureTask<HtmlText> searchTask = new FutureTask<HtmlText>(()->{
            return BaiDuUtils.getZixun(keyword);
        });
        pools.execute(searchTask);

        return  searchTask;
    }
    /*
     * 创建线程去百度搜索
     */
    public FutureTask<String>  getSearchBaiKeHtml(String keyword){
        FutureTask<String> searchTask = new FutureTask<String>(()->{
            return BaiDuUtils.getBaiKeHtml(keyword);
        });
        pools.execute(searchTask);

        return  searchTask;
    }
    /*
      创建线程数组去匹配每个选项出现的次数
     */
    public FutureTask<Integer>[] getMatchResult(String html,String[] answers){
        int length = answers.length;
        FutureTask<Integer>[] tasks =  new FutureTask[length];
        for(int index=0;index < length;index++){
            tasks[index] = new FutureTask<Integer>(new StringHandlerTask(html,answers[index]));
            //FutureTask temp = searchQuestiontasks[index];
            pools.execute(tasks[index]);
        }

        return tasks;
    }
    //获取线程执行结果
    public HtmlText getFutureResult(FutureTask<HtmlText> searchTask){
        try {
            return searchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       return null;
    }

    //获取线程执行结果
    public String getStringFromFutureResult(FutureTask<String> searchTask){
        try {
            return searchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取线程执行结果
    public int getFutureIntegerResult(FutureTask<Integer> searchTask){
        try {
            return searchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
   /*
    * 暴力匹配字符串次数
    */
   public int getStringCount(String html, List<String> listwords){
        List<Integer> restult = new ArrayList<>(listwords.size());
       for (int i = 0; i < restult.size(); i++) {
           restult.add(QuickMatch.Sunday(html,listwords.get(i)));
       }
       return restult.stream().mapToInt((item)-> item).sum();
   }

    /*
       创建线程数组去匹配每个选项出现的次数
      */
    public FutureTask<Integer> getMatchBaiKeResult(String html,List<String> listwords){

        FutureTask<Integer> task = new FutureTask<Integer>(()->{
           return getStringCount(html,listwords);
        });
            //FutureTask temp = searchQuestiontasks[index];
        pools.execute(task);


        return task;
    }
}
