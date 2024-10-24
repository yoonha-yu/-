package com.taro.tarocard;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            SiteUser user = userService.getCurrentUser();  // 로그인한 사용자 정보 가져오기
            if (user != null) {
                model.addAttribute("user", user);  // 사용자 정보를 모델에 추가
            }
        }
    }
}
