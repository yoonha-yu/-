package com.taro.tarocard.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFeedbackId(Long feedbackId); // 피드백 ID로 댓글 목록 조회
}