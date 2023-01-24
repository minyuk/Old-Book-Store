package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.ItemListResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
import com.personal.oldbookstore.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @GetMapping("")
    public Page<ItemListResponseDto> getList(Pageable pageable,
                                             @RequestParam String category,
                                             @RequestParam String keyword) {
        return itemService.getList(pageable, category, keyword);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> get(@PathVariable Long itemId,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.get(principalDetails, itemId));
    }

    @PostMapping("")
    public ResponseEntity<Long> create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @Valid @RequestPart(value = "jsonData") ItemRequestDto dto,
                                       @RequestPart(value = "fileList") List<MultipartFile> fileList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.create(principalDetails, dto, fileList));
    }

    @PatchMapping("/{itemId}")
    public void update(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @Valid @RequestBody ItemRequestDto dto) {
        itemService.update(itemId,principalDetails, dto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        itemService.delete(itemId, principalDetails);
    }


}
