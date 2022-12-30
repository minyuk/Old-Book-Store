package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.domain.user.dto.UserDTO;
import com.personal.oldbookstore.domain.user.service.UserReadService;
import com.personal.oldbookstore.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private UserWriteService userWriteService;

    private UserReadService userReadService;

    @PostMapping("")
    public ResponseEntity<Long> join(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userWriteService.join(userDTO));
    }


}
