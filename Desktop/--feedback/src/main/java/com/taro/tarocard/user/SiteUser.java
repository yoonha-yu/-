package com.taro.tarocard.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)

    private Long id;

    @Column(unique = true, nullable = false)
    private  String username;

    private String password;



    @Column(unique = true, nullable = false)
    private  String nickname;

    private String profileImageUrl;
}
