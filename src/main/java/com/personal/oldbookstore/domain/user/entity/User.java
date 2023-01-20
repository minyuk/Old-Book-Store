package com.personal.oldbookstore.domain.user.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter @ToString
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    private String provider;

    @Builder(builderClassName = "JoinForm", builderMethodName = "JoinForm")
    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    @Builder(builderClassName = "JoinOAuth2", builderMethodName = "JoinOAuth2")
    public User(String nickname, String email, String password, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserResponseDto toDto() {
        return UserResponseDto.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .build();
    }

}
