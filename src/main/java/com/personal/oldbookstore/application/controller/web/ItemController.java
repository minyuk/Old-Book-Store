package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
public class ItemController {

    @GetMapping("/write")
    public String write(){
        return "item/write";
    }

    @GetMapping("/{itemId}")
    public String detail(@PathVariable Long itemId){
        return "item/detail";
    }


}
