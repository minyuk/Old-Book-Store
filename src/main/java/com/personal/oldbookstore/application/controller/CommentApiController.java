package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.comment.dto.CommentMyPageResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentRequestDto;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentUpdateRequestDto;
import com.personal.oldbookstore.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/myList")
    public Page<CommentMyPageResponseDto> getMyList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    Pageable pageable) {
        return commentService.getMyList(principalDetails, pageable);
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<CommentResponseDto> create(@PathVariable Long itemId,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.create(principalDetails, itemId, dto));
    }

    @PatchMapping("/{commentId}")
    public void update(@PathVariable Long commentId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @Valid @RequestBody CommentUpdateRequestDto dto) {
        commentService.update(principalDetails, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        commentService.delete(principalDetails, commentId);
    }

    @GetMapping("/items/{itemId}")
    public List<CommentResponseDto> getList(@PathVariable Long itemId) {
        return commentService.getList(itemId);
    }

}
