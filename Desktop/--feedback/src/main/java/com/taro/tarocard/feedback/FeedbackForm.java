package com.taro.tarocard.feedback;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackForm {
    private String title;
    private String content;
    private int rating;
}
