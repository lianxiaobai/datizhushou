package com.ljx.zhushou.Handler;

import com.ljx.zhushou.utils.QuickMatch;

import java.util.concurrent.Callable;

public class StringHandlerTask implements Callable<Integer> {
    private String html;
    private String answer;

    public StringHandlerTask(String html,String answer){
        this.html=html;
        this.answer=answer;
    }
    @Override
    public Integer call() throws Exception {
        return QuickMatch.Sunday(html,answer);
    }
}
