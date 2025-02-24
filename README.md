# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

# Discodeit Service - User 기능 구현

## 📌 기능 구현 목록

### 1. 사용자 (User) 기능 구현 (회원가입)

#### ✅ 1-1. 사용자 생성 (회원가입)
- 새로운 사용자를 등록
- **중복 이메일 등록 시 예외 발생**
- 회원가입 시 입력 데이터:
    - `username`: **3~20자 이내, 공백 포함 불가**
    - `email`: **유효한 이메일 형식 (`@` 포함)**
    - `password`: **8자 이상, 숫자+영문 포함**
- 예외 처리:
    - **이메일이 중복되면** `IllegalArgumentException` 발생
    - **유효하지 않은 이메일 형식**이면 예외 발생
- 성공 시 반환:
    - 생성된 `User` 객체 반환 (`id`, `username`, `email`, `createdAt` 포함)

---

### ✅ 1-2. 사용자 조회 (단건 조회 / 다건 조회)
#### 🔹 1-2-1. 단건 조회 (`findById`)
- 특정 사용자 정보를 조회
- 입력값: `UUID id`
- 사용자가 존재하지 않을 경우 `IllegalArgumentException` 발생
- 반환값: `User` 객체

#### 🔹 1-2-2. 다건 조회 (`findAll`)
- 전체 사용자 리스트를 조회
- 등록된 사용자가 없을 경우 **빈 리스트 반환**
- 반환값: `List<User>`

---

### ✅ 1-3. 사용자 정보 수정
- 사용자의 `username` 또는 `password` 변경 가능
- 입력값:
    - `UUID id` (수정할 사용자)
    - `username` (선택적 변경 가능)
    - `password` (선택적 변경 가능)
- 예외 처리:
    - 존재하지 않는 사용자 ID일 경우 `IllegalArgumentException`
    - 새로운 `username`이 3~20자가 아니면 예외 발생
    - 새로운 `password`가 8자 이상이 아니면 예외 발생
- 성공 시 반환:
    - 업데이트된 `User` 객체 반환

---

### ✅ 1-4. 사용자 삭제
- 특정 사용자를 삭제
- 입력값:
    - `UUID id`
- 예외 처리:
    - 존재하지 않는 사용자 ID일 경우 `IllegalArgumentException`
- 성공 시 반환:
    - `Boolean` (삭제 성공 여부)

---

## 🔥 예외 처리 (Common)
- 모든 기능에서 공통적으로 적용
    - `IllegalArgumentException`을 활용한 검증
    - 유효성 검사 실패 시 **적절한 에러 메시지 반환**
    - 로그(`Logger`)를 통해 예외 발생 시 기록

---

## ⚙️ 테스트 목록
- **회원가입 테스트**
    - 정상적인 `User` 생성 테스트
    - 중복된 이메일로 회원가입 시 예외 발생 테스트
- **조회 기능 테스트**
    - 존재하는 사용자 조회 테스트
    - 존재하지 않는 사용자 조회 시 예외 테스트
- **수정 기능 테스트**
    - 닉네임 변경 성공 테스트
    - 비밀번호 변경 성공 테스트
    - 잘못된 값 입력 시 예외 발생 테스트
- **삭제 기능 테스트**
    - 존재하는 사용자 삭제 테스트
    - 존재하지 않는 사용자 삭제 시 예외 테스트
