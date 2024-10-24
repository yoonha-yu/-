package com.taro.tarocard.user;

import com.taro.tarocard.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/profile") // 공통 경로 지정
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final HistoryService historyService;
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserService userService, ProfileService profileService, PasswordEncoder passwordEncoder, HistoryService historyService, UserRepository userRepository) {
        this.userService = userService;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.historyService = historyService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String nickname = userDetails.getUsername();
        SiteUser user = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기
        if (user == null) {
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
        }
        model.addAttribute("user", user); // 사용자 정보를 모델에 추가
        return "profile_form"; // profile_form.html 반환
    }

    // 특정 사용자의 프로필 보기
    @GetMapping("/{id}")
    public String viewProfiles(@PathVariable("id") Long id, Model model) {
        try {
            SiteUser user = userService.getUserProfile(id);
            model.addAttribute("user", user);
            return "profile_form"; // 템플릿 이름
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error_page"; // 오류 페이지로 리다이렉트
        }
    }

    // 프로필 업데이트 폼 보기
    @GetMapping("/update")
    public String showUpdateProfileForm(Model model) {
        SiteUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", currentUser);
        return "profile_update_form"; // 프로필 업데이트 폼 템플릿
    }

    // 프로필 업데이트 처리
    @PostMapping("/update")
    public String updateProfile(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Updating profile for ID: " + id + ", Nickname: " + nickname);

            // 사용자 정보 가져오기
            SiteUser user = userService.getUserById(id);
            if (user == null) {
                throw new IllegalArgumentException("User not found for ID: " + id);
            }

            // 닉네임 업데이트
            user.setNickname(nickname);

            // 비밀번호 변경 로직
            if (password != null && !password.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }
                user.setPassword(passwordEncoder.encode(password));
            }

            // 사용자 프로필 업데이트
            profileService.updateUserProfile(user);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 성공적으로 업데이트되었습니다!");
            return "redirect:/profile/" + user.getId();
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            return "error_page";
        }
    }
}
