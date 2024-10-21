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
    private Feedback feedback; // 관련된 피드백

    @Column(nullable = false)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private String username; // 작성자 닉네임

    private LocalDateTime createdAt; // 작성일 (변경됨)

    // 생성자 추가: 피드백, 내용, 작성자 닉네임을 매개변수로 받아 초기화
    public Comment(Feedback feedback, String content, String username) {
        this.feedback = feedback;
        this.content = content;
        this.username = username;
        this.createdAt = LocalDateTime.now(); // 작성일 설정
    }

    // 추가: 작성일을 업데이트할 수 있는 메서드
    public void updateContent(String newContent) {
        this.content = newContent;
        this.createdAt = LocalDateTime.now(); // 수정 시 현재 시간으로 업데이트
    }
}

