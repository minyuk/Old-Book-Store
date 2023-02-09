package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("my")
public class MyPageController {

    @GetMapping("/info")
    public String myInfo() {
        return "/my/info";
    }

    @GetMapping("/items")
    public String myItems() {
        return "my/items";
    }

    @GetMapping("/comments")
    public String myComments() {
        return "my/comments";
    }

    @GetMapping("/favorites")
    public String myFavorites() {
        return "my/favorites";
    }

    @GetMapping("/orders")
    public String myOrders() {
        return "my/orders";
    }
}
