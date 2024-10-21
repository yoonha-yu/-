package com.taro.tarocard.card;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cardname", nullable = false, unique = true)
    private String cardname;

    @Column(name = "description")
    private String description;

    @Column(name = "categoryname")
    private String category;


}
