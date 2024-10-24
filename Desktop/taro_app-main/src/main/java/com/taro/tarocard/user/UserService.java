package com.taro.tarocard.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 일반 사용자 및 소셜 로그인 사용자 생성 메서드
    public SiteUser create(String username, String password, String nickname, String provider) {
        SiteUser user = new SiteUser();
        user.setUsername(username);

        // 소셜 로그인 사용자는 비밀번호를 설정하지 않음
        if (provider.equals("kakao")) {
            user.setPassword(""); // 소셜 로그인 시 비밀번호 공백 처리
        } else {
            user.setPassword(passwordEncoder.encode(password)); // 일반 로그인 시 비밀번호 암호화 저장
        }

        user.setNickname(nickname);
        user.setProvider("local");
        this.userRepository.save(user);
        return user;
    }
    @Transactional(readOnly=true)
    public SiteUser getUserProfile(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));
    }


    @Transactional(readOnly = true)
    public SiteUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }


    public SiteUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String username = userDetails.getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        }
        return null; // 로그인하지 않은 경우
    }

    // 사용자 프로필 업데이트 메서드
    // 사용자 프로필 업데이트
    public void updateUserProfile(SiteUser user) {
        userRepository.save(user); // user 객체를 데이터베이스에 저장하여 업데이트
    }

    @Transactional(readOnly = true)
    public SiteUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 소셜 로그인 처리 메서드
    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<SiteUser> opSiteUser = findByUsername(username);

        if (opSiteUser.isPresent()) {
            // 이미 존재하는 사용자는 기존 사용자 정보 반환
            return opSiteUser.get();
        }

        // 소셜 로그인으로 최초 로그인 시 새로운 사용자 생성
        return create(username, "", nickname, providerTypeCode);
    }

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}