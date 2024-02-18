package com.practice.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/test")
    public String videoPage() {
        return "test_index";
    }
}
