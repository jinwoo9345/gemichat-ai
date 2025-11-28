package com.example.gemichat.service;

import com.example.gemichat.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // PDF 파일, 질문 모두 처리하는 오버로드(신규)
    public ChatResponse getGeminiResponse(String userMessage, MultipartFile file) {
        String fullMessage = userMessage;

        // PDF 파일이 있으면, PDFBox로 텍스트 추출
        if (file != null && !file.isEmpty()) {
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String pdfText = stripper.getText(document);
                fullMessage += "\n문서내용: " + pdfText;
            } catch (Exception e) {
                return new ChatResponse("[PDF 읽기 오류] " + e.getMessage());
            }
        }
        // Gemini API에서 답변 가져오기 (아래 기존 메서드 활용)
        return getGeminiResponse(fullMessage);
    }

    public ChatResponse getGeminiResponse(String userMessage) {
        // gemini-1.5-flash-001 (구글이 보장하는 정식 버전 이름)
// ✨ 최신형 모델: gemini-2.5-flash
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey; 

        // 구글이 원하는 모양대로 데이터 포장하기 (복잡해 보이지만 그냥 포장지 싸는 겁니다)
        Map<String, Object> part = new HashMap<>();
        part.put("text", userMessage);
        
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        try {
            // 구글에 전송!
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            
            // 답변 껍질 까기
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
            String answerText = (String) parts.get(0).get("text");

            return new ChatResponse(answerText);
        } catch (Exception e) {
            return new ChatResponse("AI 연결 오류: " + e.getMessage());
        }
    }
}