package com.taro.tarocard;

import org.springframework.web.bind.annotation.GetMapping;

public class MainController {
    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }
}
