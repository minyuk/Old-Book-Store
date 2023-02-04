package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.ItemListResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemUpdateRequestDto;
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
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("")
    public ResponseEntity<Long> create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @Valid @RequestPart(value = "jsonData") ItemRequestDto dto,
                                       @RequestPart(value = "fileList") List<MultipartFile> fileList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.create(principalDetails, dto, fileList));
    }

    @PostMapping("/{itemId}")
    public void update(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @Valid @RequestPart(value = "jsonData") ItemUpdateRequestDto dto,
                       @RequestPart(value = "saveFileList", required = false) List<MultipartFile> saveFileList,
                       @RequestPart(value = "removeFileList", required = false) List<String> removeFileList) {
        itemService.update(itemId, principalDetails, dto, saveFileList, removeFileList);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        itemService.delete(itemId, principalDetails);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> get(@PathVariable Long itemId,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.get(principalDetails, itemId));
    }

    @GetMapping("")
    public Map<String, Object> getList(Pageable pageable,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) String keyword) {
        return itemService.getList(pageable, category, keyword);
    }

    @GetMapping("/my")
    public Page<ItemListResponseDto> getMyList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               Pageable pageable) {
        return itemService.getMyList(principalDetails, pageable);
    }
}
