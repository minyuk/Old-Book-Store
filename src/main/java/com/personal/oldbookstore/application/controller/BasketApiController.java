package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketRequestDto;
import com.personal.oldbookstore.domain.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/baskets")
@RestController
public class BasketApiController {

    private final BasketService basketService;

    @PostMapping("/items/{itemId}")
    public ResponseEntity<Long> create(@PathVariable Long itemId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @RequestBody BasketRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.create(principalDetails.getUser(), itemId, dto));
    }

}
