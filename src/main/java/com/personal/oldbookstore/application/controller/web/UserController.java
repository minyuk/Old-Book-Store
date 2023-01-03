package com.personal.oldbookstore.application.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginForm(){
        return "user/login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "user/join";
    }

    @GetMapping("/joinOk")
    public String joinOk(){
        return "user/joinOk";
    }

}
