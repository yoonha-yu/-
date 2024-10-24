package com.taro.tarocard.feedback;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/create")
    public String addComment(@RequestParam("feedbackId") Long feedbackId,
                             @RequestParam("content") String content,
                             Principal principal
    ) {
        Feedback feedback = feedbackService.findById(feedbackId);
        SiteUser user = userService.getUser(principal.getName());

        if (user == null) {
            return "redirect:/login";
        }

        if (feedback == null) {
            return "redirect:/error";
        }

        String nickname = user.getNickname();

        commentService.saveComment(feedbackId, content, nickname);

        return "redirect:/feedback";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/update/{commentId}")
    public String updateComment(@PathVariable("commentId") Long commentId,
                                @RequestParam("content") String content,
                                Principal principal) {
        commentService.updateComment(commentId, content, principal.getName());
        return "redirect:/feedback";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, Principal principal) {
        commentService.deleteComment(commentId, principal.getName());
        return "redirect:/feedback"; // 삭제 후 피드백 페이지로 리다이렉트
    }

    @GetMapping("/feedback/details/{id}")
    public String getFeedbackDetails(@PathVariable("id") Long id, Model model) {
        Feedback feedback = feedbackService.findById(id);
        List<Comment> comments = commentService.findByFeedbackIdOrderByCreatedAtDesc(id);

        model.addAttribute("feedback", feedback);
        model.addAttribute("comments", comments);
        return "feedback_page";
    }
}