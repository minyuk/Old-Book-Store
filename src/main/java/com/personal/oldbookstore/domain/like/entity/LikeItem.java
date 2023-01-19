package com.personal.oldbookstore.domain.like.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.like.dto.LikeItemResponseDto;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class LikeItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public LikeItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public LikeItemResponseDto toDto() {
        return LikeItemResponseDto.builder()
                .id(id)
                .name(item.getName())
                .bookTitle(item.getBookTitle())
                .price(item.getPrice())
                .build();
    }
}
