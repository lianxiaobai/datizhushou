package com.ljx.zhushou.serviceimpl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.Handler.StringHandlerTask;
import com.ljx.zhushou.Handler.VisitBaiDuHandler;
import com.ljx.zhushou.Service.ResultService;
import com.ljx.zhushou.bean.TextString;
import com.ljx.zhushou.utils.BaiDuUtils;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.*;

@Service("resultService")
public class ResultServiceImpl implements ResultService {
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("search").build();
    private ExecutorService pools = new ThreadPoolExecutor(10, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
   private static String sss = "";
    @Value("${htmlurl.sogourl}")
    String sogouurl;

    public TextString getResult(){
        ResultDto resultDto = new ResultDto();
        VisitBaiDuHandler visitBaiDuHandler = new VisitBaiDuHandler(pools);
        //从搜狗获取题目和选项
        TextString textString = BaiDuUtils.getText(sogouurl);

        if(sss.equals(textString.getQuestion())){
            return textString;
        }
        sss = textString.getQuestion();
        resultDto.setTitle(textString.getQuestion());
        int length = textString.getAnswers().length;
        //搜索关键内容
        String[] keywords = new String[3];
        //百度搜索结果html
        String[] ResultHtmls = new String[3];
        //访问百度线程
        FutureTask<String>[] futureTasks = new FutureTask[3];
        //匹配字符串线程
        FutureTask<Integer>[][] futureStringTasks = new FutureTask[3][length];
        //题目
        keywords[0] = textString.getQuestion();
        //题目+选项
        keywords[1] = BaiDuUtils.questionHandler(textString.getQuestion()) + " " + StringUtils.join(textString.getAnswers());
        //选项+题目
        keywords[2] = StringUtils.join(textString.getAnswers())+ " " +  BaiDuUtils.questionHandler(textString.getQuestion());
        //开始百度搜索
        for(int i = 0; i<futureTasks.length;i++){
            futureTasks[i] = visitBaiDuHandler.getSearchHtml(keywords[i]);
        }

        //获取搜索结果html
        for (int i = 0; i < ResultHtmls.length; i++) {
            ResultHtmls[i] = visitBaiDuHandler.getFutureResult(futureTasks[i]);
        }
        //开始匹配选项
        for (int i = 0; i < ResultHtmls.length; i++) {
            futureStringTasks[i] = visitBaiDuHandler.getMatchResult(ResultHtmls[i],textString.getAnswers());
        }

        //匹配结果
        int[][] searchQuestionResult = new int[3][length];
        //CountDownLatch countDownLatch = new CountDownLatch(searchQuestionResult.length);
        for (int i = 0;i < futureStringTasks.length;i++){
            final int f = i;
            //pools.execute(()-> {
                for (int j = 0;j < futureStringTasks[f].length;j++){
                   // System.out.println(f+" "+j);
                    searchQuestionResult[f][j] = visitBaiDuHandler.getFutureIntegerResult(futureStringTasks[f][j]);
                }
           // });
           // countDownLatch.countDown();
        }
       /* try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

      String[] answerResults = new String[3];
      for (int j = 0;j <answerResults.length;j++){
          String temp = "";
          for(int i = 0;i <searchQuestionResult[j].length;i++){
              temp += searchQuestionResult[j][i] + " ---------------  ";
          }
          answerResults[j] = temp;
          System.out.println(answerResults[j]);
      }
      resultDto.setResults(answerResults);
      textString.setResults(answerResults);
      return textString;
    }

    @Override
    public TextString getFirstResult() {
        return BaiDuUtils.getText(sogouurl);
    }

}
