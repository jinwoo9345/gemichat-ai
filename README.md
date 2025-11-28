# 🤖 GemiChat - AI 문서 분석 & 채팅 서비스

Java Spring Boot와 Google Gemini API(v2.5)를 활용한 **AI 기반 채팅 및 PDF 문서 요약 서비스**입니다.
단순한 텍스트 대화뿐만 아니라, RAG(검색 증강 생성)의 기초가 되는 **PDF 파일 분석 기능**을 구현했습니다.

## 🛠️ Tech Stack (기술 스택)
- **Backend:** Java 17, Spring Boot 3.3.x
- **AI Model:** Google Gemini 2.5 Flash (via REST API)
- **PDF Processing:** Apache PDFBox 2.0.29
- **Frontend:** HTML5, CSS3, Vanilla JS (Fetch API)
- **Build Tool:** Gradle

## ✨ Key Features (핵심 기능)
1. **AI 채팅 (Chatbot):**
   - 사용자가 질문을 입력하면 Gemini AI와 실시간으로 대화할 수 있습니다.
   - 라이브러리 의존도를 낮추기 위해 `RestTemplate`을 사용하여 HTTP 통신을 직접 구현했습니다.

2. **PDF 문서 요약 (Document Analysis):**
   - 사용자가 PDF 파일을 업로드하면 `Apache PDFBox`를 사용해 텍스트를 추출합니다.
   - 추출된 내용을 AI에게 전달하여 문서를 요약하거나 문서 내용 기반의 질문에 답변합니다.

## 📂 Project Structure (폴더 구조)
src/main/java/com/example/gemichat
├── controller  # ChatController (API 접수처)
├── service     # ChatService (Gemini 통신 & PDF 파싱 핵심 로직)
├── dto         # ChatRequest, ChatResponse (데이터 박스)
└── GemichatApplication.java

## 🚀 How to Run (실행 방법)

### 1. API Key 설정
`src/main/resources/application.properties` 파일을 열고 구글 Gemini API 키를 입력하세요.

[ application.properties ]
spring.application.name=gemichat
# 여기에 본인의 API 키를 붙여넣으세요
gemini.api.key=YOUR_API_KEY_HERE

### 2. 서버 실행
프로젝트 루트 경로(터미널)에서 아래 명령어를 입력하세요.

[ Terminal ]
./gradlew bootRun

### 3. 접속
서버가 실행되면 브라우저를 열고 아래 주소로 접속하세요.
- URL: http://localhost:8080

---
### 📝 Note
이 프로젝트는 AI 포트폴리오용으로 제작되었으며, 복잡한 설정 없이 바로 실행 가능하도록 설계되었습니다.