package com.taro.tarocard.card;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CardController {
    @Autowired
    private final CardService cardService;
    @Autowired
    private final RomanticCardRepository romanticCardRepository;

    @GetMapping
    public String getCategories(Model model) {
        List<Category> categories = cardService.getAllCategories();
        model.addAttribute("categories", categories);
        return "view_page"; // 변경할 HTML 템플릿 이름
    }


    @GetMapping("/romantic")
    public String romanticTaro (Model model) {
        List<RomanticCard> rcCardId = cardService.getRandomRomanticCard();
        model.addAttribute("rcCardId", rcCardId);
        return "cardchoise_page";
    }

    @GetMapping("/romantic/result/{rcid}")
    public String getCardResult(@PathVariable("rcid") Integer rcid, Model model) {
        Optional<RomanticCard> rcCard = romanticCardRepository.findById(rcid);
        if (rcCard.isPresent()) {
            RomanticCard card = rcCard.get();
            model.addAttribute("rcCard", card);
            model.addAttribute("imagePath", card.getImage_path());
            return "cardresult_page"; // 정상적인 결과 페이지
        }else {
            throw new RuntimeException("카드를 찾을 수 없습니다.");
        }
    }
}