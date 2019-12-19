package com.ljx.zhushou.serviceimpl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.Handler.StringHandlerTask;
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
    private static String sss = "";
    private ExecutorService pools = new ThreadPoolExecutor(8, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Value("${htmlurl.sogourl}")
    String sogouurl;
    public TextString getResult(){
        ResultDto resultDto = new ResultDto();
        //从搜狗获取题目和选项
        TextString textString = BaiDuUtils.getText(sogouurl);

        if(sss.equals(textString.getQuestion())){
            return textString;
        }
        sss = textString.getQuestion();
        resultDto.setTitle(textString.getQuestion());
        //调用百度搜索题目，返回搜索结果页面，并解析
        FutureTask<String> searchQuestion = new FutureTask<String>(()->{
            return BaiDuUtils.getZixun(textString.getQuestion());
        });
        pools.execute(searchQuestion);
        //调用百度搜索选项+题目，返回搜索结果页面，并解析
        FutureTask<String> searchAnsersQuestion = new FutureTask<String>(()->{
            return BaiDuUtils.getZixun(StringUtils.join(textString.getAnswers())+ " " +  BaiDuUtils.questionHandler(textString.getQuestion()));
        });
        pools.execute(searchAnsersQuestion);
        String searchQuestionHtml ="";

        try {
            searchQuestionHtml = searchQuestion.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        //对于每个选项都创建一个任务去匹配搜索网页中出现的次数
        int length = textString.getAnswers().length;
        int[] searchQuestionResult = new int[length];
        int[] searchAnsersQuestionResult = new int[length];
        FutureTask<Integer>[] searchQuestiontasks = new FutureTask[length];
        FutureTask<Integer>[] searchAnsersQuestiontasks = new FutureTask[length];

        String searchAnsersQuestionHtml="";
        try {
            searchAnsersQuestionHtml = searchAnsersQuestion.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        for(int index=0;index < length;index++){
            searchQuestiontasks[index] = new FutureTask<Integer>(new StringHandlerTask(searchQuestionHtml,textString.getAnswers()[index]));
            //FutureTask temp = searchQuestiontasks[index];
            pools.execute(searchQuestiontasks[index]);
        }
      /*  int  index=0;
        for(FutureTask task: searchAnsersQuestiontasks){
            task = new FutureTask(new StringHandlerTask(searchAnsersQuestionHtml,textString.getAnswers()[index++]));
            pools.execute(task);
        }*/
        for(int index=0;index < length;index++){
            searchAnsersQuestiontasks[index] = new FutureTask<Integer>(new StringHandlerTask(searchAnsersQuestionHtml,textString.getAnswers()[index]));
            //FutureTask temp = searchQuestiontasks[index];
            pools.execute(searchAnsersQuestiontasks[index]);
        }
        for (int i = 0; i < length; i++) {
            while (true) {
                if (searchQuestiontasks[i].isDone()) {
                    break;
                }
            }
            try {
                searchQuestionResult[i] = (int)searchQuestiontasks[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < length; i++) {
            while (true) {
                if (searchAnsersQuestiontasks[i].isDone()) {
                    break;
                }
            }
            try {
                searchAnsersQuestionResult[i] = (int)searchAnsersQuestiontasks[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
      String[] answerResults = new String[length];
      for (int j = 0;j <length;j++){
           answerResults[j] = textString.getAnswers()[j]+" : " + searchQuestionResult[j] + " ------  "+ searchAnsersQuestionResult[j];
          System.out.println(answerResults[j]);
      }
        resultDto.setResults(answerResults);
        return textString;
    }

    @Override
    public TextString getFirstResult() {
        return BaiDuUtils.getText(sogouurl);
    }

}
