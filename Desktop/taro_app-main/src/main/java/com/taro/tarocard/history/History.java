package com.taro.tarocard.history;

import com.taro.tarocard.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser user;

    @Column(nullable = false)
    private Long cardId;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public History(SiteUser user, Long cardId, LocalDateTime savedAt) {
        this.user = user;
        this.cardId = cardId;
        this.savedAt = savedAt;
    }
}
