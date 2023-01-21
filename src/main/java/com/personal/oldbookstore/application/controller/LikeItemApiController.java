package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.like.dto.LikeItemResponseDto;
import com.personal.oldbookstore.domain.like.service.LikeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/likes")
@RestController
public class LikeItemApiController {

    private final LikeItemService likeItemService;

    @GetMapping("")
    public Page<LikeItemResponseDto> getList(@AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable) {
        return likeItemService.getList(principalDetails.getUser().getId(), pageable);
    }

    @DeleteMapping("/items/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeItemService.delete(principalDetails, itemId);
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<Long> create(@PathVariable Long itemId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(likeItemService.create(principalDetails, itemId));
    }
}
