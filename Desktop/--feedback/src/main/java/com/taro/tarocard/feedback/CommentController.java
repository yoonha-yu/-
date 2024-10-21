package com.taro.tarocard.feedback;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final FeedbackService feedbackService;
    private final UserService userService;

    public CommentController(CommentService commentService, FeedbackService feedbackService, UserService userService) {
        this.commentService = commentService;
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @PostMapping("/comment/create")
    public String addComment(@RequestParam Long feedbackId,
                             @RequestParam String content,
                             @AuthenticationPrincipal SiteUser user) {
        Feedback feedback = feedbackService.findById(feedbackId);
        if (feedback == null) {
            return "redirect:/error"; // 피드백이 없을 경우 에러 페이지로 리다이렉트
        }

        String nickname = user.getNickname(); // User 클래스에서 닉네임 가져오기

        CommentForm form = new CommentForm();
        form.setContent(content);
        form.setUsername(nickname); // 댓글에 사용자 닉네임 설정
        commentService.saveComment(feedbackId, form); // 댓글 저장

        return "redirect:/feedback/details?id=" + feedbackId; // 피드백 상세 페이지로 리다이렉트
    }

    @GetMapping("/feedback/details")
    public String getFeedbackDetails(@RequestParam Long id, Model model) {
        Feedback feedback = feedbackService.findById(id);
        List<Comment> comments = commentService.findByFeedbackIdOrderByCreatedAtDesc(id); // 댓글 목록 가져오기

        model.addAttribute("feedback", feedback);
        model.addAttribute("comments", comments);
        return "feedback_page"; // 피드백 상세 페이지로 포워딩
    }
}

