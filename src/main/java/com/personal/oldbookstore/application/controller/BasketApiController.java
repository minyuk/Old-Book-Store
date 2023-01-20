package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.domain.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/baskets")
@RestController
public class BasketApiController {

    private final BasketService basketService;

    @PostMapping("/items/{itemId}")

}
