package com.taro.tarocard.history;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    private final UserService userService;

    @PostMapping("/saveHistory")
    public String saveHistory(@RequestParam Long cardId, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        History history = new History(user, cardId, LocalDateTime.now());
        historyService.save(history);
        return "redirect:/history";
    }

    @GetMapping("/history")
    public String viewHistory(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<History> histories = historyService.findByUserId(user.getId());
        model.addAttribute("histories", histories);
        return "history_page";
    }
}
