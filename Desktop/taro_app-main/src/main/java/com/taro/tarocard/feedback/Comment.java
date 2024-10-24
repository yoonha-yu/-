package com.taro.tarocard.feedback;

import com.taro.tarocard.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    private int commentCount;


    public Comment(Feedback feedback, String content, String username, String nickname) {
        this.feedback = feedback;
        this.content = content;
        this.username = username;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
        this.commentCount = 0;
    }


    public void updateContent(String newContent) {
        this.content = newContent;
        this.createdAt = LocalDateTime.now();
    }
    public void updateCommentCount(int count){
        this.commentCount = count;
    }
}