package com.personal.oldbookstore.domain.order.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.basket.repository.BasketRepository;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.entity.SaleStatus;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.order.dto.OrderItemRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.entity.Order;
import com.personal.oldbookstore.domain.order.entity.OrderStatus;
import com.personal.oldbookstore.domain.order.entity.Payment;
import com.personal.oldbookstore.domain.order.repository.OrderItemRepository;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BasketRepository basketRepository;

    private User user;
    private PrincipalDetails principalDetails;
    private Item item1, item2;
    private List<OrderItemRequestDto> orderItemRequestDtos = new ArrayList<>();

    @BeforeEach
    void createUserWithItem() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);
        item1 = saveItem(user, "습관 만들어요", "DEVELOPMENT", "아주 작은 습관의 힘", "제임스 클리어", "미개봉 제품", 100, 6000);
        item2 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 1, 10000);
    }

    @Test
    @DisplayName("주문 취소 성공 - 상품 여러 개, 상품 상태 변경")
    void cancelManyItemWithUpdateSaleStatus() {
        //given
        OrderItemRequestDto orderItemDto1 = createOrderItemDto(item1.getId(), 10);
        orderItemRequestDtos.add(orderItemDto1);
        OrderItemRequestDto orderItemDto2 = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto2);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");
        Long orderId = orderService.create(principalDetails, request);

        //when
        orderService.cancel(orderId);

        //then
        assertThat(item1.getSaleStatus()).isEqualTo(SaleStatus.SALE);
        assertThat(item2.getSaleStatus()).isEqualTo(SaleStatus.SALE);
    }

    @Test
    @DisplayName("주문 취소 성공 - 상품 여러 개, 상품 재고 증가")
    void cancelManyItemWithIncrementStock() {
        //given
        OrderItemRequestDto orderItemDto1 = createOrderItemDto(item1.getId(), 10);
        orderItemRequestDtos.add(orderItemDto1);
        OrderItemRequestDto orderItemDto2 = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto2);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");
        Long orderId = orderService.create(principalDetails, request);

        //when
        orderService.cancel(orderId);

        //then
        assertThat(item1.getStock()).isEqualTo(100);
        assertThat(item2.getStock()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 취소 성공 - 주문 목록 삭제하지 않고 주문 상태 변경")
    void cancelNotDeleteWithUpdateOrderStatus() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item1.getId(), 10);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");
        Long orderId = orderService.create(principalDetails, request);

        //when
        orderService.cancel(orderId);

        //then
        Order order = orderRepository.findByIdWithFetchJoinOrderItem(orderId).orElse(null);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("상품 주문 실패 - 재고 부족")
    void orderFailStock() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item1.getId(), 1000);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        //then
        assertThrows(CustomException.class, () -> {
            orderService.create(principalDetails, request);
        });
    }

    @Test
    @DisplayName("상품 주문 성공 - 주문한 상품 장바구니에서 삭제")
    void orderDeleteBasket() {
        //given
        createBasket(principalDetails.getUser(), item1, 2);
        createBasket(principalDetails.getUser(), item2, 1);

        OrderItemRequestDto orderItemDto1 = createOrderItemDto(item1.getId(), 2);
        orderItemRequestDtos.add(orderItemDto1);
        OrderItemRequestDto orderItemDto2 = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto2);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        orderService.create(principalDetails, request);

        //then
        assertThat(basketRepository.findByUserIdAndItemId(principalDetails.getUser().getId(), item1.getId()).orElse(null)).isNull();
        assertThat(basketRepository.findByUserIdAndItemId(principalDetails.getUser().getId(), item2.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("상품 주문 성공 - 총 금액 계산")
    void orderManyItemCalcTotalPrice() {
        //given
        OrderItemRequestDto orderItemDto1 = createOrderItemDto(item1.getId(), 2);
        orderItemRequestDtos.add(orderItemDto1);
        OrderItemRequestDto orderItemDto2 = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto2);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        Long orderId = orderService.create(principalDetails, request);

        //then
        Order order = orderRepository.findByIdWithFetchJoinOrderItem(orderId).orElse(null);
        assertThat(order.getTotalPrice()).isEqualTo(22000);
    }

    @Test
    @DisplayName("상품 주문 성공 - 주문 상태 변경")
    void orderUpdateOrderStatus() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item1.getId(), 1);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        Long orderId = orderService.create(principalDetails, request);

        //then
        Order order = orderRepository.findByIdWithFetchJoinOrderItem(orderId).orElse(null);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    @DisplayName("상품 주문 성공 - 상품 상태 변경")
    void orderUpdateSaleStatus() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        orderService.create(principalDetails, request);

        //then
        assertThat(item2.getSaleStatus()).isEqualTo(SaleStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("상품 주문 성공 - 상품 재고 감소")
    void orderDecreaseStock() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item1.getId(), 1);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        orderService.create(principalDetails, request);

        //then
        assertThat(item1.getStock()).isEqualTo(99);
    }

    @Test
    @DisplayName("상품 주문 성공 - 상품 여러 개")
    void orderManyItem() {
        //given
        OrderItemRequestDto orderItemDto1 = createOrderItemDto(item1.getId(), 2);
        orderItemRequestDtos.add(orderItemDto1);
        OrderItemRequestDto orderItemDto2 = createOrderItemDto(item2.getId(), 1);
        orderItemRequestDtos.add(orderItemDto2);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        Long orderId = orderService.create(principalDetails, request);

        //then
        Order order = orderRepository.findByIdWithFetchJoinOrderItem(orderId).orElse(null);
        assertThat(order.getRecipient()).isEqualTo("tester");
        assertThat(order.getOrderItems().get(0).getItem().getId()).isEqualTo(item1.getId());
        assertThat(order.getOrderItems().get(1).getItem().getId()).isEqualTo(item2.getId());
    }

    @Test
    @DisplayName("상품 주문 성공 - 상품 한 개")
    void orderOneItem() {
        //given
        OrderItemRequestDto orderItemDto = createOrderItemDto(item1.getId(), 2);
        orderItemRequestDtos.add(orderItemDto);

        OrderRequestDto request = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");

        //when
        Long orderId = orderService.create(principalDetails, request);

        //then
        Order order = orderRepository.findByIdWithFetchJoinOrderItem(orderId).orElse(null);
        assertThat(order.getRecipient()).isEqualTo("tester");
        assertThat(order.getOrderItems().get(0).getItem().getId()).isEqualTo(item1.getId());
    }

    @Test
    @DisplayName("장바구니 상품 불러오기")
    void load() {
        //given
        Basket basket1 = createBasket(principalDetails.getUser(), item1, 1);
        Basket basket2 = createBasket(principalDetails.getUser(), item2, 1);

        List<Long> itemIds = new ArrayList<>();
        itemIds.add(basket1.getItem().getId());
        itemIds.add(basket2.getItem().getId());
        principalDetails.setItemIdList(itemIds);

        //when
        List<BasketResponseDto> basketList = orderService.getBasketList(principalDetails.getItemIdList());

        //then
        assertThat(basketList.size()).isEqualTo(2);
    }

    private Basket createBasket(User user, Item item, Integer count) {
        Basket basket = Basket.builder()
                .user(user)
                .item(item)
                .count(count)
                .build();

        return basketRepository.save(basket);
    }

    private OrderItemRequestDto createOrderItemDto(Long itemId, Integer count) {
        return new OrderItemRequestDto(itemId, count);
    }

    private OrderRequestDto createOrderDto(List<OrderItemRequestDto> items, String recipient, String phone, String payment) {
        return OrderRequestDto.builder()
                .orderItems(items)
                .recipient(recipient)
                .phone(phone)
                .address(null)
                .payment(Payment.valueOf(payment))
                .build();
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
