package com.personal.oldbookstore.config.oauth;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.config.oauth.userinfo.GoogleUserInfo;
import com.personal.oldbookstore.config.oauth.userinfo.NaverUserInfo;
import com.personal.oldbookstore.config.oauth.userinfo.Oauth2UserInfo;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            oauth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String email = oauth2UserInfo.getEmail();
        String defaultNickname = oauth2UserInfo.getProvider() + "_user" + UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("password" + UUID.randomUUID().toString().substring(0, 6));

        User findUser = userRepository.findByEmail(email).orElse(null);

        if(findUser == null) {
            findUser = User.JoinOAuth2()
                    .nickname(defaultNickname)
                    .password(password)
                    .email(email)
                    .provider(provider).build();

            userRepository.save(findUser);
        }

        return new PrincipalDetails(findUser, oauth2UserInfo);
    }
}
