package com.personal.oldbookstore.application.controller;

import com.personal.oldbookstore.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommonApiController {

    private final ItemService itemService;

    @GetMapping("/image/{imageUrl}")
    public byte[] getFile(@PathVariable String imageUrl) {
        return itemService.getFile(imageUrl);
    }
}
