package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.comment.dto.CommentRequestDto;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentUpdateRequestDto;
import com.personal.oldbookstore.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public void update(@PathVariable Long commentId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @Valid @RequestBody CommentUpdateRequestDto dto) {
        commentService.update(principalDetails, commentId, dto);
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<CommentResponseDto> create(@PathVariable Long itemId,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.create(principalDetails, itemId, dto));
    }
}
