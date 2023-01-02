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

    private String passwordConfirm;

    @Builder
    public UserRequestDto(String nickname, String email, String password, String passwordConfirm) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
