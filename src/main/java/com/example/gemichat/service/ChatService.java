package com.example.gemichat.service;

import com.example.gemichat.dto.ChatResponse;
import com.example.gemichat.entity.ChatEntity; // 1. Entity 가져오기
import com.example.gemichat.repository.ChatRepository; // 2. Repository 가져오기
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor // 3. 창고지기를 자동으로 데려오는 롬복 설정
public class ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ChatRepository chatRepository; // 4. 창고지기 고용 (의존성 주입)

    // 파일 없이 채팅만 할 때
    public ChatResponse getGeminiResponse(String userMessage) {
        return getGeminiResponse(userMessage, null);
    }

    // 파일 포함 채팅 (핵심 로직)
    public ChatResponse getGeminiResponse(String userMessage, MultipartFile file) {
        String fullPrompt = userMessage;

        // 1. PDF 파일이 있으면 텍스트 추출해서 질문 뒤에 붙이기
        if (file != null && !file.isEmpty()) {
            try {
                String pdfText = extractTextFromPdf(file);
                fullPrompt += "\n\n[참고 문서 내용]:\n" + pdfText;
            } catch (IOException e) {
                return new ChatResponse("PDF 읽기 실패: " + e.getMessage());
            }
        }

        // 2. Gemini API 호출 (기존 코드와 동일)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> part = new HashMap<>();
        part.put("text", fullPrompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
            String aiAnswer = (String) parts.get(0).get("text");

            // ==========================================
            // 👇 [핵심] 여기서 DB에 저장합니다!
            // ==========================================
            ChatEntity chatEntity = new ChatEntity(userMessage, aiAnswer); // 대화 내용 포장
            chatRepository.save(chatEntity); // 창고지기에게 "저장해!" 명령
            // ==========================================

            return new ChatResponse(aiAnswer);

        } catch (Exception e) {
            return new ChatResponse("AI 연결 오류: " + e.getMessage());
        }
    }

    // PDF 텍스트 추출 헬퍼 함수
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    public List<ChatEntity> getAllHistory() {
        return chatRepository.findAll(); // 창고지기한테 "다 가져와!" 시키기
    }
}