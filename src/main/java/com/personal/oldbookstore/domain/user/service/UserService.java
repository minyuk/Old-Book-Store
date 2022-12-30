package com.personal.oldbookstore.domain.user.service;

import com.personal.oldbookstore.domain.user.dto.UserRequestDto;
import com.personal.oldbookstore.domain.user.dto.UserResponseDto;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto join(UserRequestDto userRequestDto) {
        validateEmail(userRequestDto.getEmail());
        validateNickname(userRequestDto.getNickname());

        User user = User.JoinForm()
                .nickname(userRequestDto.getNickname())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .build();

        return userRepository.save(user).toDto();
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        validateNickname(nickname);

        if (user.getNickname().equals(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_EQUAL_PREVIOUS);
        }

        user.updateNickname(nickname);
    }

    public boolean validateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
                    throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
                }
        );

        return true;
    }

    public boolean validateNickname(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(user -> {
                    throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
                }
        );

        return true;
    }

}
