package com.personal.oldbookstore.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponseDto {

    private Long id;
    private String nickname;
    private String email;

    @Builder
    public UserResponseDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
