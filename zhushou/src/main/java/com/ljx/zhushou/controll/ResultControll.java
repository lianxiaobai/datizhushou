package com.ljx.zhushou.controll;


import com.ljx.zhushou.Base.BaseResponse;
import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.Service.ResultService;
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
    public BaseResponse<ResultDto> getResult(){
       //resultService.getResult();
        BaseResponse<ResultDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(resultService.getResult());
       return baseResponse;
    }
}
