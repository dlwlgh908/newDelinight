package com.onetouch.delinight.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@RestController
@RequestMapping("/sse")
public class SseRestController {

    @GetMapping("/connect")
    public SseEmitter connect(Principal principal){
        return null;
    }
}
