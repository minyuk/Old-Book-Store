package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.service.OrderService;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/{orderId}")
    public void cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
    }

    @PostMapping("")
    public ResponseEntity<Long> create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @Valid @RequestBody OrderRequestDto dto) {
        User user = principalDetails.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(orderService.create(user, dto));
    }
}
