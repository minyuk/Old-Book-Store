package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/basket")
    public String basket() {
        return "order/basket";
    }

    @GetMapping("/order")
    public String order() {
        return "order/order";
    }


}
