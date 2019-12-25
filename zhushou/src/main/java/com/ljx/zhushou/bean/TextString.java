package com.ljx.zhushou.bean;

import lombok.Data;

@Data
public class TextString {
    private String question;
    private String[] answers;
    private String[] results;
    private String SoGouAnswer;
    private String LiSiAnswer;
    private String selectAnswer;
}
