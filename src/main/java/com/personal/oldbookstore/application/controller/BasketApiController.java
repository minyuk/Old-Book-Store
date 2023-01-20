package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketRequestDto;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/baskets")
@RestController
public class BasketApiController {

    private final BasketService basketService;

    @GetMapping("")
    public Page<BasketResponseDto> getList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           Pageable pageable) {
        return basketService.getList(principalDetails.getUser(), pageable);
    }

    @DeleteMapping("/items/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        basketService.delete(principalDetails.getUser(), itemId);
    }

    @PatchMapping("/items/{itemId}")
    public void update(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @RequestBody BasketRequestDto dto) {
        basketService.update(principalDetails.getUser(), itemId, dto);
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<Long> create(@PathVariable Long itemId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @RequestBody BasketRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.create(principalDetails, itemId, dto));
    }

}
