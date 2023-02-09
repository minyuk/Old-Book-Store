package com.personal.oldbookstore.domain.item.entity;

import com.personal.oldbookstore.domain.item.dto.ItemFileResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ItemFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_file_id")
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemFile(Long id, Item item, String imageUrl) {
        this.id = id;
        this.item = item;
        this.imageUrl = imageUrl;
    }

    public ItemFileResponseDto toDto() {
        return ItemFileResponseDto.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
