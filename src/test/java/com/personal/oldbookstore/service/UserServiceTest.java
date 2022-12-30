package com.personal.oldbookstore.service;

import com.personal.oldbookstore.domain.user.dto.UserRequestDto;
import com.personal.oldbookstore.domain.user.dto.UserResponseDto;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.domain.user.service.UserService;
import com.personal.oldbookstore.util.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("닉네임 수정 실패 - 이전 닉네임과 동일")
    void updateNicknameEqualPrevious() {
        //given
        UserRequestDto request = createUser("test@abc.com", "test1!", "tester");
        UserResponseDto response = userService.join(request);

        //when

        //then
        assertThrows(CustomException.class, () -> {
            userService.updateNickname(response.getId(), "tester");
        });
    }

    @Test
    @DisplayName("닉네임 수정 실패 - 존재하지 않는 회원")
    void updateNicknameNotFoundUser() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            userService.updateNickname(1L, "updateTester");
        });
    }

    @Test
    @DisplayName("닉네임 수정 성공")
    void updateNickname() {
        //given
        UserRequestDto request = createUser("test@abc.com", "test1!", "tester");
        UserResponseDto response = userService.join(request);

        //when
        userService.updateNickname(response.getId(), "updateTester");

        //then
        User user = userRepository.findById(response.getId()).orElse(null);
        assertThat(user.getNickname()).isEqualTo("updateTester");
    }


    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void joinDuplicateEmail() {
        //given
        UserRequestDto request = createUser("test@abc.com", "test1!", "tester1");
        userService.join(request);

        UserRequestDto request2 = createUser("test@abc.com", "test123!!", "tester2");

        //when

        //then
        assertThrows(CustomException.class, () -> {
            userService.join(request2);
        });

    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void joinDuplicateNickname() {
        //given
        UserRequestDto request = createUser("test1@abc.com", "test1!", "tester");
        userService.join(request);

        UserRequestDto request2 = createUser("test2@abc.com", "test123!!", "tester");

        //when

        //then
        assertThrows(CustomException.class, () -> {
            userService.join(request2);
        });
    }

    @Test
    @DisplayName("회원가입 성공")
    void join() {
        //given
        UserRequestDto request = createUser("test@abc.com", "test1!", "tester");

        //when
        userService.join(request);

        //then
        assertThat(userRepository.count()).isEqualTo(1);

        User user = userRepository.findByEmail("test@abc.com").orElse(null);
        assertThat(user.getNickname()).isEqualTo("tester");
    }
    private static UserRequestDto createUser(String email, String password, String nickname) {
        return UserRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
