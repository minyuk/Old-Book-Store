package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.order.dto.OrderListResponseDto;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderResponseDto;
import com.personal.oldbookstore.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping("")
    public Page<OrderListResponseDto> getList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              Pageable pageable) {
        return orderService.getList(principalDetails, pageable);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> get(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.get(orderId));
    }

    @PostMapping("/{orderId}")
    public void cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
    }

    @PostMapping("")
    public ResponseEntity<Long> create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @Valid @RequestBody OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.create(principalDetails, dto));
    }

    @GetMapping("/load")
    public List<BasketResponseDto> load(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return orderService.getBasketList(principalDetails.getItemIdList());
    }


    @PostMapping("/add")
    public void basketToOrder(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              @RequestBody Map<String, List<Long>> map) {
        principalDetails.setItemIdList(map.get("itemIds"));
    }
}
