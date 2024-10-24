package com.taro.tarocard.feedback;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @GetMapping("")
    public String listFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getlist();
        model.addAttribute("feedbacks", feedbacks);
        return "feedback_page";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showNewFeedbackForm(Model model) {
        model.addAttribute("feedbackForm", new FeedbackForm());
        return "feedback_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createFeedback(@ModelAttribute FeedbackForm form, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("인증된 사용자가 아닙니다.");
        }
        SiteUser user = userService.getUser(principal.getName());
        feedbackService.saveFeedback(form, user);

        return "redirect:/feedback";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyFeedback(@PathVariable ("id") Long id, FeedbackForm feedbackForm, Principal principal, Model model) {
        Feedback feedback = feedbackService.findById(id);
        checkFeedbackOwner(feedback, principal);
        feedbackForm.setTitle(feedback.getTitle());
        feedbackForm.setContent(feedback.getContent());

        return "feedback_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyFeedback(@PathVariable ("id") Long id, @Valid FeedbackForm feedbackForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "feedback_form";
        }

        Feedback feedback = feedbackService.findById(id);
        checkFeedbackOwner(feedback, principal);
        feedbackService.updateFeedback(id, feedbackForm, feedback);

        return "redirect:/feedback";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id, Principal principal) {
        Feedback feedback = feedbackService.findById(id);
        checkFeedbackOwner(feedback, principal);
        feedbackService.deleteFeedback(id);
        System.out.println(id);
        return "redirect:/feedback"; // 삭제 후 리다이렉트
    }

    private void checkFeedbackOwner(Feedback feedback, Principal principal) {
        if (!feedback.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정/삭제 권한이 없습니다.");
        }

    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/{id}")
    public String likeFeedback(@PathVariable("id") Long id, Principal principal){
        Feedback feedback = feedbackService.findById(id);
        feedbackService.addLike(feedback);
        return "redirect:/feedback";
    }
}