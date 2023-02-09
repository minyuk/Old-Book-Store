package com.personal.oldbookstore.domain.item.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.item.dto.ItemListResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemUpdateRequestDto;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.entity.SaleStatus;
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

import java.io.IOException;
import java.util.*;

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
    private final List<OrderItem> orderItems = new ArrayList<>();

    @BeforeEach
    void createUser() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 카테고리 and 키워드 검색")
    void getListCategoryAndKeyword() throws IOException {
        //given
        ItemRequestDto request1 = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        ItemRequestDto request2 = createItem("testing2", "TEST", "abc1", "tester2",
                "good2", 4, 2000);

        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Map<String, Object> items = itemService.getList(pageable, "TEST", "abc");

        //then
        assertThat(items.get("category")).isEqualTo("테스트");

        List<ItemListResponseDto> list = new ArrayList<>();
        for (ItemListResponseDto response : (Page<ItemListResponseDto>) items.get("pagination")) {
            list.add(response);
        }

        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 키워드(상품명 or 도서명 or 도서 저자) 검색")
    void getListKeyword() throws IOException{
        //given
        ItemRequestDto request1 = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        ItemRequestDto request2 = createItem("testing2", "TEST", "abc1", "tester2",
                "good2", 4, 2000);
        ItemRequestDto request3 = createItem("testing3", "TEST", "abc2", "tester3",
                "good3", 4, 2000);


        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);
        itemService.create(principalDetails, request3, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Map<String, Object> items = itemService.getList(pageable, null, "abc");

        //then
        assertThat(items.get("category")).isEqualTo("통합검색");

        List<ItemListResponseDto> list = new ArrayList<>();
        for (ItemListResponseDto response : (Page<ItemListResponseDto>) items.get("pagination")) {
            list.add(response);
        }

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 리스트 조회 - 카테고리")
    void getListCategory() throws IOException{
        //given
        ItemRequestDto request1 = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        ItemRequestDto request2 = createItem("testing2", "IT", "test2", "tester2",
                "good2", 4, 2000);

        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Map<String, Object> items = itemService.getList(pageable, "TEST", null);

        //then
        assertThat(items.get("category")).isEqualTo("테스트");

        List<ItemListResponseDto> list = new ArrayList<>();
        for (ItemListResponseDto response : (Page<ItemListResponseDto>) items.get("pagination")) {
            list.add(response);
        }

        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 리스트 조회 성공")
    void getList() throws IOException{
        //given
        ItemRequestDto request1 = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        ItemRequestDto request2 = createItem("testing2", "TEST", "test2", "tester2",
                "good", 4, 2000);

        itemService.create(principalDetails, request1, null);
        itemService.create(principalDetails, request2, null);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Map<String, Object> items = itemService.getList(pageable, "TEST", null);

        //then
        assertThat(items.get("category")).isEqualTo("테스트");

        List<ItemListResponseDto> list = new ArrayList<>();
        for (ItemListResponseDto response : (Page<ItemListResponseDto>) items.get("pagination")) {
            list.add(response);
        }

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세 조회 - 조회수 증가")
    void getIncrementViewCount() throws IOException{
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
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
            itemService.get(principalDetails, 0L);
        });
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void get() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        //when
        ItemResponseDto response = itemService.get(principalDetails, itemId);

        //then
        assertThat(response.getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("상품 수정 실패 - 작성자 불일치")
    void updateNotEqualUser() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

        User user2 = saveUser("test2@efg.com", "1111!!!", "tester2");
        PrincipalDetails principalDetails2 = new PrincipalDetails(user2);
        ItemUpdateRequestDto update = createUpdateItem("updateTesting", "TEST", "test123", "tester1",
                "goodbb", 1, 7000, "SALE");

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
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);

        ItemUpdateRequestDto update = createUpdateItem("updateTesting", "TEST", "test123", "tester1",
                "goodbb", 1, 7000, "SALE");

        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.update(0L, principalDetails, update, null, null);
        });
    }

    @Test
    @DisplayName("상품 수정 성공")
    void update() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);

        Long itemId = itemService.create(principalDetails, request, null);

        ItemUpdateRequestDto update = createUpdateItem("updateTesting", "TEST", "test123", "tester1",
                "goodbb", 1, 7000, "SALE");

        //when
        itemService.update(itemId, principalDetails, update, null, null);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("updateTesting");
    }

    @Test
    @DisplayName("상품 삭제 실패 - 주문 처리 된 상품")
    void deleteExistOrder() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
        Long itemId = itemService.create(principalDetails, request, null);

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
    void deleteNotEqualUser() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);
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
            itemService.delete(0L, principalDetails);
        });
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void delete() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);

        Long itemId = itemService.create(principalDetails, request, null);

        //when
        itemService.delete(itemId, principalDetails);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item).isNull();
    }

    @Test
    @DisplayName("상품 등록 성공")
    void create() throws IOException {
        //given
        ItemRequestDto request = createItem("testing", "TEST", "test", "tester",
                "good", 2, 5000);

        //when
        Long itemId = itemService.create(principalDetails, request, null);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("testing");
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

    private ItemUpdateRequestDto createUpdateItem(String name, String category, String bookTitle, String bookAuthor,
                                            String contents, Integer stock, Integer price, String saleStatus) {
        return ItemUpdateRequestDto.builder()
                .name(name)
                .category(Category.valueOf(category))
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .saleStatus(SaleStatus.valueOf(saleStatus))
                .build();
    }
}
