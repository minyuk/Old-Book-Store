package com.personal.oldbookstore.domain.comment.entity;

import com.personal.oldbookstore.util.BooleanConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    private Integer depth;

    private Long parentId;

    @Convert(converter = BooleanConverter.class)
    private Boolean viewStatus = true;

    @Builder
    public Comment(Long id, String contents, Integer depth, Long parentId, Boolean viewStatus) {
        this.id = id;
        this.contents = contents;
        this.depth = depth;
        this.parentId = parentId;
        this.viewStatus = viewStatus;
    }
}
