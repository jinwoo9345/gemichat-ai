package com.example.gemichat.entity;

import jakarta.persistence.*; // JPA 도구들 가져오기
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity // 1. "야 스프링! 이거 DB 테이블이야!" (가장 중요)
@Table(name = "chat_history") // 2. 테이블 이름은 'chat_history'로 해줘
@Getter @Setter
@NoArgsConstructor // 3. 기본 생성자 필수 (JPA 규칙)
public class ChatEntity {

    @Id // 4. 이게 주민등록번호(PK)야
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 5. 번호는 1, 2, 3... 네가 알아서 올려줘 (Auto Increment)
    private Long id;

    @Column(columnDefinition = "TEXT") // 6. 질문이 길 수도 있으니까 넉넉하게 잡아줘
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private LocalDateTime createdAt; // 언제 대화했는지 날짜 찍기

    // 우리가 쓸 생성자 (편하게 만들기 위해)
    public ChatEntity(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.createdAt = LocalDateTime.now(); // 생성될 때 현재 시간 자동 입력
    }
}