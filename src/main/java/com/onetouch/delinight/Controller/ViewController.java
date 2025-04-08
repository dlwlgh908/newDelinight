package com.onetouch.delinight.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "pages/index";
    }

    @GetMapping("/usertest")
    public String userContent() {
        return "/pages/usertest";
    }


}