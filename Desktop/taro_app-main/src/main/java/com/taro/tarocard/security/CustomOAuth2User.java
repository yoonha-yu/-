package com.taro.tarocard.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

class CustomOAuth2User extends User implements OAuth2User {
    private final Map<String, Object> attributes;

    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(username, password, authorities);
        this.attributes = attributes;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
