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
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemFile(Long id, Item item, String path, String name) {
        this.id = id;
        this.item = item;
        this.filePath = path;
        this.fileName = name;
    }

    public ItemFileResponseDto toDto() {
        return ItemFileResponseDto.builder()
                .fileName(fileName)
                .build();
    }
}
