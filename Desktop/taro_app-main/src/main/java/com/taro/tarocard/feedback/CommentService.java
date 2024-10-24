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


    public List<Comment> findByFeedbackIdOrderByCreatedAtDesc(Long feedbackId) {
        return commentRepository.findByFeedbackId(feedbackId);
    }

    // 댓글 저장 메서드
    public void saveComment(Long feedbackId, String content, String nickname) {
        Feedback feedback = feedbackService.findById(feedbackId);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setFeedback(feedback);
        comment.setUsername(nickname); // 이 부분을 nickname으로 변경
        comment.setNickname(nickname); // 닉네임도 세팅
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        feedbackService.incrementCommentCount(feedbackId);
    }


    public void updateComment(Long commentId, String content, String nickname) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUsername().equals(nickname)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        comment.setContent(content);
        comment.setUpdateAt(LocalDateTime.now());
        commentRepository.save(comment);
    }


    public void deleteComment(Long commentId, String nickname) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUsername().equals(nickname)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        Feedback feedback = comment.getFeedback();
        commentRepository.delete(comment);

        feedbackService.decrementCommentCount(feedback.getId()); // feedback.getId()로 수정
    }
}