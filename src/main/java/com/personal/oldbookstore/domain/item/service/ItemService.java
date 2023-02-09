package com.personal.oldbookstore.domain.item.service;

import com.google.cloud.storage.Bucket;
import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.*;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.entity.ItemFile;
import com.personal.oldbookstore.domain.item.repository.ItemFileRepository;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.like.repository.LikeItemRepository;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final LikeItemRepository likeItemRepository;
    private final ItemFileRepository itemFileRepository;

//    @Value("${file.dir}")
//    private String imgUploadPath;

    private final Bucket bucket;

    public Long create(PrincipalDetails principalDetails, ItemRequestDto dto, List<MultipartFile> fileList) throws IOException{
        Item item = Item.builder()
                .user(principalDetails.getUser())
                .name(dto.name())
                .category(dto.category())
                .bookTitle(dto.bookTitle())
                .bookAuthor(dto.bookAuthor())
                .contents(dto.contents())
                .stock(dto.stock())
                .price(dto.price())
                .build();

        if (fileList != null) uploadFile(item, fileList);


        return itemRepository.save(item).getId();
    }

    public void update(Long itemId, PrincipalDetails principalDetails, ItemUpdateRequestDto dto,
                       List<MultipartFile> saveFileList, List<String> removeFileList) throws IOException {
        Item item = findItem(itemId);

        if (!principalDetails.getUser().getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }

        item.updateItem(dto);

        if (saveFileList != null) uploadFile(item, saveFileList);
        if (removeFileList != null) fileRemove(removeFileList);
    }

    public void delete(Long itemId, PrincipalDetails principalDetails) {
        Item item = findItem(itemId);

        if (!principalDetails.getUser().getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.DELETE_ACCESS_DENIED);
        }

        if (!orderRepository.findAllByItemId(itemId).isEmpty()) {
            throw new CustomException(ErrorCode.DELETE_EXIST_ORDER);
        }

        itemRepository.delete(item);
    }

    public ItemResponseDto get(PrincipalDetails principalDetails, Long itemId) {
        Item item = findItem(itemId);

        item.incrementViewCount();

        ItemResponseDto itemResponseDto = item.toDto();
        if (principalDetails != null) {

            if (item.getUser().getEmail().equals(principalDetails.getUser().getEmail())) {
                itemResponseDto.isSeller();
            }

            itemResponseDto.setLikeStatus(
                    likeItemRepository.findByUserIdAndItemId(principalDetails.getUser().getId(), itemId).isPresent()
            );

        }

        return itemResponseDto;
    }

    public Map<String, Object> getList(Pageable pageable, String category, String keyword) {
        Page<ItemListResponseDto> items = itemRepository.findAllBySearchOption(pageable, category, keyword).map(Item::toDtoList);

        Map<String, Object> map = new HashMap<>();
        map.put("pagination", items);

        if(category == null) {
            category = "통합검색";
        } else {
            category = Category.valueOf(category).getValue();
        };
        map.put("category", category);

        return map;
    }

    public Page<ItemListResponseDto> getMyList(PrincipalDetails principalDetails, Pageable pageable) {
        if (principalDetails == null) {
            throw new CustomException(ErrorCode.ONLY_USER);
        }

        return itemRepository.findAllByUserId(principalDetails.getUser().getId(), pageable).map(Item::toDtoList);
    }

    public Map<String, List<ItemIndexResponseDto>> index() {
        Map<String, List<ItemIndexResponseDto>> map = new HashMap<>();

        for (Category category : Category.values()) {
            List<Item> items = itemRepository.findAllByCategory(category.toString());
            List<ItemIndexResponseDto> indexList = items.stream().map(Item::toDtoIndex).collect(Collectors.toList());

            map.put(category.toString().toLowerCase(), indexList);
        }

        return map;
    }

    // 파일 보내기
    public byte[] getFile(String imageUrl) {

        return bucket.get("files/" + imageUrl).getContent();
    }

    // 파일 입력 및 저장
    private List<String> uploadFile(Item item, List<MultipartFile> files) throws IOException {

        // File 저장위치를 선언
        String blob = "files/" ;

        // 파일을 Bucket에 저장
        List<String> urls = new ArrayList<>();

        for(MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            String url = blob + uuid;
            bucket.create(url, file.getBytes());
            urls.add("/" + url);

            ItemFile itemFile = ItemFile.builder()
                    .item(item)
                    .imageUrl(uuid).build();

            itemFileRepository.save(itemFile);
        }

        return urls;
    }

    private void fileRemove(List<String> fileList) {
        for (String imageUrl : fileList) {
            itemFileRepository.deleteByImageUrl(URLDecoder.decode(imageUrl, StandardCharsets.UTF_8));
        }
    }

    private Item findItem(Long id) {
        return itemRepository.findByIdWithFetchJoinUser(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );
    }
}
