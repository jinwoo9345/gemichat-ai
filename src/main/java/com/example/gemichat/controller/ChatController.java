package com.example.gemichat.controller;

import com.example.gemichat.dto.ChatRequest;
import com.example.gemichat.dto.ChatResponse;
import com.example.gemichat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // [변경] 파일 및 메시지 동시 업로드 지원 (multipart/form-data)
    @PostMapping(consumes = {"multipart/form-data"})
    public ChatResponse chatWithFile(
            @RequestParam(required = false) MultipartFile file, // PDF 파일 (첨부 안 해도 됨)
            @RequestParam String message // 질문 텍스트
    ) {
        return chatService.getGeminiResponse(message, file);
    }

    // [기존] JSON 요청 방식도 여전히 허용 (백워드 호환)
    @PostMapping(consumes = {"application/json"})
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.getGeminiResponse(request.getMessage());
    }
}