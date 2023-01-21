package com.personal.oldbookstore.domain.comment.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
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

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ItemRepository itemRepository;


    public Page<CommentResponseDto> getList(Long itemId, Pageable pageable) {
        return commentRepository.findAllByItemId(itemId, pageable).map(Comment::toDto);
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
