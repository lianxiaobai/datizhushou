package com.ljx.zhushou.Handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljx.zhushou.utils.BaiDuUtils;
import org.springframework.stereotype.Component;

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
    public FutureTask<String>  getSearchHtml(String keyword){
        FutureTask<String> searchTask = new FutureTask<String>(()->{
            return BaiDuUtils.getZixun(keyword);
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
    public String getFutureResult(FutureTask<String> searchTask){
        try {
            return searchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       return "";
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


}
