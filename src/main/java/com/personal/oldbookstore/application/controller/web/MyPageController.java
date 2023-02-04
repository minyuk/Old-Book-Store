package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping("/my/info")
    public String myInfo() {
        return "/my/info";
    }

    @GetMapping("/my/items")
    public String myItems() {
        return "my/items";
    }

    @GetMapping("/my/comments")
    public String myComments() {
        return "my/comments";
    }
}
