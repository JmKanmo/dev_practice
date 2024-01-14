package com.network.network_project.video.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {
    @GetMapping("/")
    public String page() {
        return "index";
    }

    @GetMapping("/upload_video")
    public String videoPage() {
        return "upload_video";
    }
}
