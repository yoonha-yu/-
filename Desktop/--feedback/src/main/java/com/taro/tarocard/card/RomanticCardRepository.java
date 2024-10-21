package com.taro.tarocard.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RomanticCardRepository extends JpaRepository<RomanticCard, Integer> {

    Optional<RomanticCard> findAllByRcid(Integer rcid);
}
