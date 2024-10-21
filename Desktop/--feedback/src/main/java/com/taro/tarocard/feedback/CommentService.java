package com.taro.tarocard.feedback;

import com.taro.tarocard.user.UserService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final FeedbackService feedbackService;

    // 특정 피드백에 대한 댓글 목록을 작성일 기준으로 내림차순 정렬하여 가져오는 메서드
    public List<Comment> findByFeedbackIdOrderByCreatedAtDesc(Long feedbackId) {
        return commentRepository.findByFeedbackId(feedbackId);
    }

    // 댓글 저장 메서드
    public void saveComment(Long feedbackId, CommentForm form) {
        Feedback feedback = feedbackService.findById(feedbackId); // 피드백 객체 가져오기
        Comment comment = new Comment();
        comment.setContent(form.getContent());
        comment.setFeedback(feedback); // 피드백 객체 설정
        comment.setUsername(userService.getCurrentUser().getNickname()); // 작성자 닉네임 설정
        comment.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        commentRepository.save(comment); // 댓글 저장
    }


    // 댓글 수정 메서드
    public void updateComment(Long commentId, CommentForm form) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUsername().equals(userService.getCurrentUser().getNickname())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        comment.setContent(form.getContent());
        commentRepository.save(comment); // 수정된 댓글 저장
    }

    // 댓글 삭제 메서드
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUsername().equals(userService.getCurrentUser().getNickname())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment); // 댓글 삭제
    }
}
