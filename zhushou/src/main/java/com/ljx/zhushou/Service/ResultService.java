package com.ljx.zhushou.Service;

import com.ljx.zhushou.Dto.ResultDto;
import com.ljx.zhushou.bean.TextString;
import org.springframework.stereotype.Service;


public interface ResultService {
    public TextString getResult();
    public TextString getFirstResult();
}
