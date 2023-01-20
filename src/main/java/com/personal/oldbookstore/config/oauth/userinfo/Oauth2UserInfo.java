package com.personal.oldbookstore.config.oauth.userinfo;

import java.util.Map;

public interface Oauth2UserInfo {

    Map<String, Object> getAttributes();

    String getProviderId();

    String getProvider();
    
    String getEmail();

    String getName();
}
