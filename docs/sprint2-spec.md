# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

# Discodeit Service - File lo 를 통한 데이터 영속화

## 📌 기능 구현 목록

### 1. 저장 로직

### ✅ 1-1. 저장하는 Repository 만들기 
- 저장 로직 과 관련된 기능을 도메인 모델 별 인터페이스
- 네이밍 규칙:
    - [도메인 모델 이름] Repository
---

### ✅ 1-2. 리포토리 인터페이스 구현체
- JCF[도메인 모델 이름] Repository 는 기존 자료구조 활용해서 구현
- File[도메인 모델 이름] Repository 는 직렬화, 역직렬화 활용

### 🔹 1-2-2. File[도메인 모델 이름] Repository
- ##### 직렬 저장
  - ObjectOutputStream을 반환하는 메서드
  - 기존 ObjectOutputStream의 헤더 중복 문제를 해결하는 클래스
