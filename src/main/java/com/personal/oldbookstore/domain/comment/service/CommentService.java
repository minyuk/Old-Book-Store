package com.personal.oldbookstore.domain.comment.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.comment.dto.CommentMyPageResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentRequestDto;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentUpdateRequestDto;
import com.personal.oldbookstore.domain.comment.entity.Comment;
import com.personal.oldbookstore.domain.comment.repository.CommentRepository;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ItemRepository itemRepository;

    public Page<CommentMyPageResponseDto> getMyList(PrincipalDetails principalDetails, Pageable pageable) {
        return commentRepository.findAllByUserId(principalDetails.getUser().getId(), pageable).map(Comment::toMyDto);
    }

    public CommentResponseDto create(PrincipalDetails principalDetails, Long itemId, CommentRequestDto dto) {
        if (principalDetails == null) {
            throw new CustomException(ErrorCode.ONLY_USER);
        }

        Item item = findItem(itemId);

        Comment comment = Comment.builder()
                .user(principalDetails.getUser())
                .item(item)
                .contents(dto.contents())
                .depth(dto.depth())
                .parentId(dto.parentId())
                .build();

        item.incrementCommentCount();

        return commentRepository.save(comment).toDto();
    }

    public void update(PrincipalDetails principalDetails, Long commentId, CommentUpdateRequestDto dto) {
        Comment comment = findComment(commentId);

        validateUser(principalDetails.getUser(), comment.getUser());

        comment.updateContents(dto.contents());
    }

    public void delete(PrincipalDetails principalDetails, Long commentId) {
        Comment comment = findComment(commentId);

        validateUser(principalDetails.getUser(), comment.getUser());

        comment.updateViewStatusFalse();
    }

    public List<CommentResponseDto> getList(Long itemId) {
        return commentRepository.findAllByItemId(itemId).stream().map(Comment::toDto).collect(Collectors.toList());
    }

    private void validateUser(User loginUser, User writer) {
        if (!loginUser.getEmail().equals(writer.getEmail())) {
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.EDIT_ACCESS_DENIED)
        );
    }

    private Item findItem(Long id) {
        return itemRepository.findByIdWithFetchJoinUser(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );
    }
}
