package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.domain.user.dto.UserRequestDto;
import com.personal.oldbookstore.domain.user.dto.UserResponseDto;
import com.personal.oldbookstore.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.join(userRequestDto));
    }


}
