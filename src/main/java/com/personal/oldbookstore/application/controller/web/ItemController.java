package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = {"/list", "/list/{category}"})
    public String list(@PathVariable(required = false) String category,
                       @RequestParam(value = "page", required = false) String page){

        if(category == null){
            category = "";
        }else{
            category = "&category=" + category;
        }

        if(page == null){
            return "redirect:/item/list?page=1" + category;
        }

        return "item/list";
    }

}
