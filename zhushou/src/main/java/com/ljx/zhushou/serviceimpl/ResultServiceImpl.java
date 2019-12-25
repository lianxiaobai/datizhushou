package com.ljx.zhushou.serviceimpl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.Handler.StringHandlerTask;
import com.ljx.zhushou.Handler.VisitBaiDuHandler;
import com.ljx.zhushou.Service.ResultService;
import com.ljx.zhushou.bean.HtmlText;
import com.ljx.zhushou.bean.TextString;
import com.ljx.zhushou.utils.ArrayUs;
import com.ljx.zhushou.utils.BaiDuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service("resultService")
@Slf4j
public class ResultServiceImpl implements ResultService {
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("search").build();
    private ExecutorService pools = new ThreadPoolExecutor(10, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
   private static String sss = "";
   private static String prowords="";
    @Value("${htmlurl.sogourl}")
    String sogouurl;

    public TextString getResult(String words){
        ResultDto resultDto = new ResultDto();
        VisitBaiDuHandler visitBaiDuHandler = new VisitBaiDuHandler(pools);
        //从搜狗获取题目和选项
        TextString textString = BaiDuUtils.getText(sogouurl);
        //百度分词结果
        List<String> listWords = new ArrayList<>();
        if (words !=null && words !=""){
            prowords = words;
            System.out.println("prowords:--" + prowords);
        }
        if(sss.equals(textString.getQuestion())){
            return textString;
        }
        sss = textString.getQuestion();
        textString.setQuestion(textString.getQuestion().substring(textString.getQuestion().indexOf(".")+1));
        resultDto.setTitle(textString.getQuestion());
        //处理提示词，以逗号分隔
        if (prowords !=null && prowords !=""){
            System.out.println("prowords:" + prowords);
            textString.setQuestion(prowords + " " + textString.getQuestion());
        }
        int length = textString.getAnswers().length;
        //搜索关键内容
        String[] keywords = new String[5];
        //百度搜索结果html
        String[] ResultHtmls = new String[keywords.length];
        //百度搜索结果html
        String[] ResultBaiKeHtmls = new String[length];
        //访问百度线程
        FutureTask<HtmlText>[] futureTasks = new FutureTask[keywords.length];
        //访问百度百科线程
        FutureTask<String>[] futureBaiKeTasks = new FutureTask[length];
        //匹配字符串线程
        FutureTask<Integer>[][] futureStringTasks = new FutureTask[keywords.length][length];
        //题目
        keywords[0] = textString.getQuestion();
         //题目+选项
        //keywords[1] = BaiDuUtils.questionHandler(textString.getQuestion()) + " " + StringUtils.join(textString.getAnswers());
        int position = 14>textString.getQuestion().length()?textString.getQuestion().length():14;
        keywords[1] = textString.getQuestion().substring(0,position);
        int positiontemp = position;
        //选项+题目
        //keywords[2] = StringUtils.join(textString.getAnswers())+ " " +  BaiDuUtils.questionHandler(textString.getQuestion());
        position = 24>textString.getQuestion().length()?textString.getQuestion().length():24;
        keywords[2] = textString.getQuestion().substring(positiontemp,position);
        positiontemp = position;
        position = 34>textString.getQuestion().length()?textString.getQuestion().length():34;
        keywords[3] = textString.getQuestion().substring(positiontemp,position);
        keywords[4] = BaiDuUtils.questionHandler(textString.getQuestion()) + " " + StringUtils.join(textString.getAnswers());
        //keywords[0] = keywords[4];
        //开始百度搜索
        for(int i = 0; i<futureTasks.length;i++){
            futureTasks[i] = visitBaiDuHandler.getSearchHtml(keywords[i]);
        }
        //开始百度百科搜索
        for(int i = 0; i<length;i++){
            futureBaiKeTasks[i] = visitBaiDuHandler.getSearchBaiKeHtml(textString.getAnswers()[i]);
        }

      /*  int[] prompt = new int[length];
        for (int i = 0; i < length; i++) {
            prompt[i] = BaiDuUtils.getPromptWords(textString.getAnswers()[i],textString.getQuestion());
        }*/
        //获取搜索结果html
        for (int i = 0; i < ResultHtmls.length; i++) {
            ResultHtmls[i] = visitBaiDuHandler.getFutureResult(futureTasks[i]).getHtml();
        }

        //开始匹配选项
        for (int i = 0; i < ResultHtmls.length; i++) {
            futureStringTasks[i] = visitBaiDuHandler.getMatchResult(ResultHtmls[i],textString.getAnswers());
        }

        //匹配结果
        int[][] searchQuestionResult = new int[keywords.length][length];
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

      String[] answerResults = new String[keywords.length];
      for (int j = 0;j <answerResults.length;j++){
          String temp = "";
          for(int i = 0;i <searchQuestionResult[j].length;i++){
              temp += searchQuestionResult[j][i] + " ---------------  ";
          }
          answerResults[j] = temp;
          System.out.println(answerResults[j]);
      }
       /* String LiSiAnswer = "";
        int LiSiPosition;
        int i,j;
        for (i = 0; i < searchQuestionResult.length; i++) {
            int temp =0;
            if(i==0){
                LiSiPosition = 0;
                int max = 0;
                int min = 0;
                for (j = 0; j <length ; j++) {
                    temp += searchQuestionResult[i][j];

                    LiSiPosition
                }
                if(temp>0) break;
            }
            for (int j = 0; j <searchQuestionResult[i].length ; j++) {
                temp += searchQuestionResult[i][j];
            }
            if(temp==0){

            }else {

                break;
            }

        }*/
        listWords = visitBaiDuHandler.getFutureResult(futureTasks[0]).getList();

        FutureTask<Integer>[] MatchBaiKeFutureTask = new FutureTask[length];
        //获取搜索结果html
        for (int i = 0; i < ResultBaiKeHtmls.length; i++) {
            ResultBaiKeHtmls[i] = visitBaiDuHandler.getStringFromFutureResult(futureBaiKeTasks[i]);
        }
        //获取匹配结果
        for (int i = 0; i < length; i++) {
            MatchBaiKeFutureTask[i] = visitBaiDuHandler.getMatchBaiKeResult(ResultBaiKeHtmls[i],listWords);
        }

        String LiShiAnswer="";
        int[] sumAnswers = new int[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < searchQuestionResult.length; j++) {
                sumAnswers[i] += searchQuestionResult[j][i];
            }
        }

      /* int[] temp = new int[length];
       if(ArrayUs.max(searchQuestionResult[0])>0){
           if (textString.getQuestion().indexOf("不")>0 || textString.getQuestion().indexOf("没有")>0){
               LiShiAnswer = textString.getAnswers()[ArrayUs.minIndex(searchQuestionResult[0])];
           }else {
               LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(searchQuestionResult[0])];
           }
       }else if(ArrayUs.max(searchQuestionResult[4])>0){
           for (int i = 0; i < length; i++) {
               temp[i] = searchQuestionResult[1][i]+searchQuestionResult[2][i]+searchQuestionResult[3][i];
           }
           if (textString.getQuestion().indexOf("不")>0 || textString.getQuestion().indexOf("没有")>0){
               LiShiAnswer = textString.getAnswers()[ArrayUs.minIndex(temp)];
           }else {
               LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(temp)];
           }
          // LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(temp)];
       }else {
           if (textString.getQuestion().indexOf("不")>0 || textString.getQuestion().indexOf("没有")>0){
               LiShiAnswer = textString.getAnswers()[ArrayUs.minIndex(searchQuestionResult[4])];
           }else {
               LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(searchQuestionResult[4])];
           }
           //LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(searchQuestionResult[4])];
       }*/
        int[] selectAnswer = new int[length];
        for (int i = 0; i < length; i++) {
            selectAnswer[i] = visitBaiDuHandler.getFutureIntegerResult(MatchBaiKeFutureTask[i]);
        }
        if(ArrayUs.max(sumAnswers)>0){
            if (textString.getQuestion().indexOf("不")>0 || textString.getQuestion().indexOf("没有")>0){
                LiShiAnswer = textString.getAnswers()[ArrayUs.minIndex(searchQuestionResult[0])];
            }else {
                LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(searchQuestionResult[0])];
            }
        }else {
            if (textString.getQuestion().indexOf("不")>0 || textString.getQuestion().indexOf("没有")>0){
                LiShiAnswer = textString.getAnswers()[ArrayUs.minIndex(selectAnswer)];
            }else {
                LiShiAnswer = textString.getAnswers()[ArrayUs.maxIndex(selectAnswer)];
            }

        }
      resultDto.setResults(answerResults);
      textString.setResults(answerResults);
      textString.setLiSiAnswer(LiShiAnswer);
      //textString.setSelectAnswer(textString.getAnswers()[ArrayUs.maxIndex(selectAnswer)]);
      System.out.println("推荐答案：" + LiShiAnswer);
      System.out.println("搜狗答案：" + textString.getSoGouAnswer());
      System.out.println("题目：" +textString.getQuestion());
      System.out.println("选项：" + Arrays.toString(textString.getAnswers()));
      //System.out.println("选项答案：" + textString.getSelectAnswer());
      return textString;
    }

    @Override
    public TextString getFirstResult() {
        return BaiDuUtils.getText(sogouurl);
    }

}
