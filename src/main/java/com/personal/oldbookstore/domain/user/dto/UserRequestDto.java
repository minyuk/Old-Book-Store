package com.personal.oldbookstore.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequestDto {

    private String nickname;

    private String email;

    private String password;

    @Builder
    public UserRequestDto(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
