package com.taro.tarocard.security;

import com.taro.tarocard.user.SiteUser;
import com.taro.tarocard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) attributesProperties.get("nickname");

        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String username = String.format("%s__%s", providerTypeCode, oauthId);

        // 사용자 로그인 처리 및 provider 설정
        SiteUser siteUser = userService.whenSocialLogin(providerTypeCode, username, nickname);
        siteUser.setProvider(providerTypeCode); // provider 설정

        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 필요한 권한 추가 (예: ROLE_USER)
        // authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomOAuth2User(siteUser.getUsername(), siteUser.getPassword(), authorityList, attributes);
    }
}
