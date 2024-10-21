package com.taro.tarocard.cardlist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardList {
    @GetMapping("/card/list")
    public String showCardList(){

        return "card_list_form";
    }
}
