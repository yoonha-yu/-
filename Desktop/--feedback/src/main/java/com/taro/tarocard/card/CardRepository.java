package com.taro.tarocard.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findAllById(Integer id);
    List<Card> findByCategory(String category);
}
