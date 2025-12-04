package com.example.gemichat.repository;

import com.example.gemichat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// <관리할 테이블(Entity), 주민번호 타입(ID Type)>
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    // 아무것도 안 적어도 됩니다.
    // 스프링 부트가 알아서 save(저장), findAll(조회), delete(삭제) 기능을 다 만들어줍니다.
}