package com.taro.tarocard.user;

import com.taro.tarocard.history.History;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<History> historys;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String provider;
    // 추가된 프로필 사진 URL 필드
    private String profileImageUrl; // 프로필 사진 URL

}
