package com.taro.tarocard.feedback;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentForm {
    private String content;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
}