package com.personal.oldbookstore.domain.like.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.like.dto.LikeItemResponseDto;
import com.personal.oldbookstore.domain.like.entity.LikeItem;
import com.personal.oldbookstore.domain.like.repository.LikeItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class LikeItemServiceTest {

    @Autowired
    private LikeItemService likeItemService;

    @Autowired
    private LikeItemRepository likeItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private PrincipalDetails principalDetails;

    @BeforeEach
    void createUserWithItem() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);
    }

    @Test
    @DisplayName("관심상품 리스트 조회 성공 - 페이징")
    void getListPage() {
        //given
        Item item1 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        Item item2 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 1, 10000);
        likeItemService.create(principalDetails, item1.getId());
        likeItemService.create(principalDetails, item2.getId());

        Pageable pageable = PageRequest.of(1, 1);

        //when
        Page<LikeItemResponseDto> likeItemList = likeItemService.getList(user.getId(), pageable);

        //then
        assertThat(likeItemList.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("관심상품 리스트 조회 성공")
    void getList() {
        //given
        Item item1 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        Item item2 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 1, 10000);
        likeItemService.create(principalDetails, item1.getId());
        likeItemService.create(principalDetails, item2.getId());

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<LikeItemResponseDto> likeItemList = likeItemService.getList(user.getId(), pageable);

        //then
        assertThat(likeItemList.get().count()).isEqualTo(2);
    }

    @Test
    @DisplayName("관심상품 삭제 실패 - 존재하지 않는 상품")
    void deleteNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            likeItemService.delete(principalDetails, 1L);
        });
    }

    @Test
    @DisplayName("관심상품 삭제 성공 - 관심상품 카운트 감소")
    void deleteDecreaseLikeCount() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        likeItemService.create(principalDetails, item.getId());

        //when
        likeItemService.delete(principalDetails, item.getId());

        //then
        Item findItem = itemRepository.findByIdWithFetchJoinUser(item.getId()).orElse(null);
        assertThat(findItem.getLikeCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("관심상품 삭제 성공")
    void delete() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        Long id = likeItemService.create(principalDetails, item.getId());

        //when
        likeItemService.delete(principalDetails, item.getId());

        //then
        assertThat(likeItemRepository.findById(id).orElse(null)).isNull();
    }

    @Test
    @DisplayName("관심상품 등록 실패 - 비회원")
    void createOnlyUser() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            likeItemService.create(null, item.getId());
        });
    }

    @Test
    @DisplayName("관심상품 등록 실패 - 존재하지 않는 상품")
    void createNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            likeItemService.create(principalDetails, 1L);
        });
    }

    @Test
    @DisplayName("관심상품 등록 성공 - 관심상품 카운트 증가")
    void createIncrementLikeCount() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);

        //when
        likeItemService.create(principalDetails, item.getId());

        //then
        Item findItem = itemRepository.findByIdWithFetchJoinUser(item.getId()).orElse(null);
        assertThat(findItem.getLikeCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("관심상품 등록 성공")
    void create() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);

        //when
        Long id = likeItemService.create(principalDetails, item.getId());

        //then
        LikeItem likeItem = likeItemRepository.findByIdWithFetchJoinItem(id).orElse(null);
        assertThat(likeItem.getItem().getStock()).isEqualTo(100);
    }

    private Item saveItem(User user, String name, String category, String bookTitle, String bookAuthor,
                          String contents, Integer stock, Integer price) {
        return itemRepository.save(Item.builder()
                .user(user)
                .name(name)
                .category(Category.valueOf(category))
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .build());
    }

    private User saveUser(String email, String password, String nickname) {
        return userRepository.save(User.JoinForm()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());
    }
}