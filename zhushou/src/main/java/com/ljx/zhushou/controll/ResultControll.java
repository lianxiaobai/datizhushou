package com.ljx.zhushou.controll;


import com.ljx.zhushou.Base.BaseResponse;
import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.Service.ResultService;
import com.ljx.zhushou.bean.TextString;
import com.ljx.zhushou.utils.BaiDuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ResultControll {
    @Resource
    ResultService resultService;
    @GetMapping("/getResult")
    public TextString getResult(){
       //resultService.getResult();
        //BaseResponse<ResultDto> baseResponse = new BaseResponse<>();
        //baseResponse.setData(resultService.getResult());
       return resultService.getResult();
    }

    @GetMapping("/getFirstResult")
    public TextString getFirstResult(){
        //return resultService.getFirstResult();
        TextString textString = new TextString();
        textString.setQuestion("12. 俗语“有眼不识泰山”最早记载于哪本名著？");
        textString.setAnswers(new String[]{"西游记","水浒传","三国演义","红楼梦"});

        return textString;
    }
}
