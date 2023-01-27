package com.personal.oldbookstore.domain.item.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.ItemListResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemFileRepository;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.order.entity.Order;
import com.personal.oldbookstore.domain.order.entity.OrderItem;
import com.personal.oldbookstore.domain.order.entity.Payment;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemFileRepository itemFileRepository;

    private User user;
    private PrincipalDetails principalDetails;
    private List<OrderItem> orderItems = new ArrayList<>();

    @BeforeEach
    void createUser() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 카테고리 and 키워드 검색")
    void getListCategoryAndKeyword() {
        //given
        ItemRequestDto request1 = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 10, 10000);
        ItemRequestDto request2 = createItem("알고리즘 인터뷰", "IT", "혼자 공부하는 자바", "미녁",
                "독학하기 좋아요", 2, 8000);

        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ItemListResponseDto> items = itemService.getList(pageable, "IT", "미녁");

        //then
        assertThat(items.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 키워드(상품명 or 도서명 or 도서 저자) 검색")
    void getListKeyword() {
        //given
        ItemRequestDto request1 = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 10, 10000);
        ItemRequestDto request2 = createItem("알고리즘 인터뷰", "IT", "혼자 공부하는 자바", "저자",
                "독학하기 좋아요", 2, 8000);
        ItemRequestDto request3 = createItem("습관 만들어요", "DEVELOPMENT", "아주 작은 습관의 힘",
                "제임스 클리어", "가독성이 좋아요", 5, 5000);


        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);
        itemService.create(principalDetails, request3, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ItemListResponseDto> items = itemService.getList(pageable, null, "자바");

        //then
        assertThat(items.get().count()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 카테고리")
    void getListCategory() {
        //given
        ItemRequestDto request1 = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 10, 10000);
        ItemRequestDto request2 = createItem("습관 만들어요", "DEVELOPMENT", "아주 작은 습관의 힘",
                "제임스 클리어", "가독성이 좋아요", 5, 5000);

        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ItemListResponseDto> items = itemService.getList(pageable, "IT", null);

        //then
        assertThat(items.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 리스트 조회 성공")
    void getList() {
        //given
        ItemRequestDto request1 = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 10, 10000);
        ItemRequestDto request2 = createItem("습관 만들어요", "DEVELOPMENT", "아주 작은 습관의 힘",
                "제임스 클리어", "가독성이 좋아요", 5, 5000);

        Long itemId = itemService.create(principalDetails, request1, null);
        Long itemId2 = itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ItemListResponseDto> items = itemService.getList(pageable, null, null);

        //then
        assertThat(items.get().count()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세 조회 - 조회수 증가")
    void getIncrementViewCount() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        //when
        itemService.get(principalDetails, itemId);
        itemService.get(principalDetails, itemId);
        itemService.get(principalDetails, itemId);
        itemService.get(principalDetails, itemId);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getViewCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("상품 상세 조회 실패 - 존재하지 않는 상품")
    void getNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.get(principalDetails, 1L);
        });
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void get() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        //when
        ItemResponseDto response = itemService.get(principalDetails, itemId);

        //then
        assertThat(response.getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("상품 수정 실패 - 작성자 불일치")
    void updateNotEqualUser() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        User user2 = saveUser("test2@efg.com", "1111!!!", "tester2");
        PrincipalDetails principalDetails2 = new PrincipalDetails(user2);
        ItemRequestDto update = createItem("상품명 수정합니다", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
           itemService.update(itemId, principalDetails2, update, null, null);
        });
    }

    @Test
    @DisplayName("상품 수정 실패 - 존재하지 않는 상품")
    void updateNotFound() {
        //given
        ItemRequestDto request = createItem("상품명 수정합니다", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.update(1L, principalDetails, request, null, null);
        });
    }

    @Test
    @DisplayName("상품 수정 성공")
    void update() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        Long itemId = itemService.create(principalDetails, request, null);

        ItemRequestDto update = createItem("상품명 수정합니다", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        itemService.update(itemId, principalDetails, update, null, null);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("상품명 수정합니다");
    }

    @Test
    @DisplayName("상품 삭제 실패 - 주문 처리 된 상품")
    void deleteExistOrder() {
        //given
        ItemRequestDto itemRequestDto = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(principalDetails, itemRequestDto, null);

        OrderItem orderItem = createOrderItem(itemId, 1);
        orderItems.add(orderItem);
        createOrder(user, orderItems, "tester", "01012345678", "CARD");

        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.delete(itemId, principalDetails);
        });
    }

    @Test
    @DisplayName("상품 삭제 실패 - 작성자 불일치")
    void deleteNotEqualUser() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        User user2 = saveUser("test2@efg.com", "1111!!!", "tester2");
        PrincipalDetails principalDetails2 = new PrincipalDetails(user2);

        //when
        //then
        assertThrows(CustomException.class, () -> {
           itemService.delete(itemId, principalDetails2);
        });
    }

    @Test
    @DisplayName("상품 삭제 실패 - 존재하지 않는 상품")
    void deleteNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.delete(1L, principalDetails);
        });
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void delete() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        Long itemId = itemService.create(principalDetails, request, null);

        //when
        itemService.delete(itemId, principalDetails);

        //then
        assertThat(itemRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품 등록 성공")
    void create() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        Long itemId = itemService.create(principalDetails, request, null);

        //then
        assertThat(itemRepository.count()).isEqualTo(1);

        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("자바 팔아요");
        assertThat(item.getPrice()).isEqualTo(5000);
    }

    private OrderItem createOrderItem(Long itemId, Integer count) {
        Item item = itemRepository.findByIdWithFetchJoinUser(itemId).orElse(null);

        return OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();
    }

    private void createOrder(User user, List<OrderItem> orderItems, String recipient, String phone, String payment) {
        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .recipient(recipient)
                .phone(phone)
                .payment(Payment.valueOf(payment))
                .address(null)
                .build();

        orderRepository.save(order);
    }

    private User saveUser(String email, String password, String nickname) {
        return userRepository.save(User.JoinForm()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());
    }

    private ItemRequestDto createItem(String name, String category, String bookTitle, String bookAuthor,
                            String contents, Integer stock, Integer price) {
        return ItemRequestDto.builder()
                .name(name)
                .category(Category.valueOf(category))
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .build();
    }
}
