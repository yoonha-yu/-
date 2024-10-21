package com.taro.tarocard.card;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "romantic_card")
public class RomanticCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rcid;

    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "categoryname")
    private String categoryname;

    @Column(name = "description")
    private String description;
}
