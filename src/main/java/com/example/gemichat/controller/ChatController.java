package com.example.gemichat.controller;

import com.example.gemichat.dto.ChatResponse;
import com.example.gemichat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.example.gemichat.entity.ChatEntity;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 파일 업로드와 채팅을 동시에 처리하는 컨트롤러
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ChatResponse chat(
        @RequestParam(value = "file", required = false) MultipartFile file,    // 프론트엔드에서 보낸 이름 "file"
        @RequestParam("message") String message) {     // 프론트엔드에서 보낸 이름 "message"
        
        // 서비스(직원)에게 전달
        return chatService.getGeminiResponse(message, file);
    }

    @GetMapping // 1. "주소창으로 들어오거나, 조회 요청할 때 여기로!"
    public List<ChatEntity> getHistory() {
        return chatService.getAllHistory(); // 2. 쉐프한테 받아온 장부를 그대로 줌
    }
}