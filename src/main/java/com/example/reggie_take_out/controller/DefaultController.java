package com.example.reggie_take_out.controller;

import com.example.reggie_take_out.common.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String defaultpage(){
        return "/front/page/login.html";
    }

}
