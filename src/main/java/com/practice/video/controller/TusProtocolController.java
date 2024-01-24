package com.practice.video.controller;

import com.practice.video.service.TusProtocolService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TusProtocolController {
    private final TusProtocolService tusProtocolService;

    // POST ,PATCH 모두 지원 되도록...
    @RequestMapping(value = {"/tus/upload/video", "/tus/upload/video/**"})
    public ResponseEntity<String> uploadVideo(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tusProtocolService.tusUpload(request, response));
        } catch (Exception e) {
            log.error("uploadVideo api error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
