package similarity.impl;

import com.baidu.aip.nlp.AipNlp;
import org.json.JSONObject;
import similarity.Similarity;
import utils.QuickMatch;

import java.util.HashMap;

/**
 * Created by lingfengsan on 2018/1/23.
 *
 * @author lingfengsan
 */
public class BaiDuSimilarity implements Similarity {
    private static AipNlp client;
    private String question;
    private String answer;
    BaiDuSimilarity(String question,String answer){
        this.question=question;
        this.answer=answer;
    }

    @Override
    public Integer call() {
        int count = QuickMatch.Sunday(question,answer);
        return count;
    }



    public static void main(String[] args) {
        BaiDuSimilarity baiDuSimilarity = new BaiDuSimilarity("浙富股份","万事通自考网");
        double score = baiDuSimilarity.call();
        System.out.println(score);
    }

    public static void setClient(AipNlp client) {
        BaiDuSimilarity.client=client;
    }
}
