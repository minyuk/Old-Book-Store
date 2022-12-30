package com.personal.oldbookstore.domain.user.service;

import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean joinEmailDuplicateCheck(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
                    throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
                }
        );

        return true;
    }

    public boolean joinNicknameDuplicateCheck(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(user -> {
                    throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
                }
        );

        return true;
    }


}
