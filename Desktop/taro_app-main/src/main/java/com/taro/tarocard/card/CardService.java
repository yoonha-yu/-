package com.taro.tarocard.card;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private JdbcTemplate jdbcTemplate;
    private final RomanticCardRepository romanticCardRepository;
    private final CategoryRepository categoryRepository;

    public List<Card> getCard () {
        return this.cardRepository.findAll();
    }
    public Card getCardById (Integer id){
        return this.cardRepository.findAllById(id);
    }

    public Optional<RomanticCard> getCardByRcCardId(Integer rcCardId){
        return romanticCardRepository.findAllByRcid(rcCardId);
    }

    public List<RomanticCard> getRandomRomanticCard() {
        List<RomanticCard> rcCard = romanticCardRepository.findAll();
        System.out.println("Fetched Cards: " + rcCard);
        Collections.shuffle(rcCard);
        return rcCard;
    }

    public List<Category> getAllCategories () {
        return categoryRepository.findAll();
    }
    public List<Category> getCategoryByName(String categoryname){
        return categoryRepository.findByCategoryname(categoryname);
    }

}
