package com.taro.tarocard.user;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;

    @Transactional
    public void updateUserProfile(SiteUser user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("프로필 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


}
