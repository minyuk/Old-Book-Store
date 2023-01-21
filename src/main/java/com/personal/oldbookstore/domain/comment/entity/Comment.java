package com.personal.oldbookstore.domain.comment.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.BooleanConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private String contents;

    private Integer depth;

    private Long parentId;

    @Convert(converter = BooleanConverter.class)
    private boolean viewStatus;

    @Builder
    public Comment(User writer, Item item, String contents, Integer depth, Long parentId, Boolean viewStatus) {
        this.writer = writer;
        this.item = item;
        this.contents = contents;
        this.depth = depth;
        this.parentId = parentId;
        this.viewStatus = viewStatus == null || viewStatus;
    }

    public CommentResponseDto toDto() {
        return CommentResponseDto.builder()
                .id(id)
                .writer(writer.getNickname())
                .contents(setContents())
                .depth(depth)
                .parentId(parentId)
                .viewStatus(viewStatus)
                .createdDate(getModifiedDate())
                .build();
    }

    public void updateViewStatusFalse() {
        this.viewStatus = false;
    }

    private String setContents() {
        if (!viewStatus) this.contents = "삭제된 댓글입니다.";

        return contents;
    }
}
