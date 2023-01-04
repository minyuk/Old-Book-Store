package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("")
    public ResponseEntity<Long> create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @RequestBody ItemRequestDto dto) {

        Long itemId = itemService.create(principalDetails.getUser(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(itemId);
    }

    @PatchMapping("{itemId}")
    public void update(@PathVariable Long itemId,
                       @RequestBody ItemRequestDto dto) {
        itemService.update(itemId, dto);
    }

}
