# Part1-sprint4

## 기본 요구사항

1. git 브랜치 관리

- [x] 브랜치 변경 : part1-황지환-sprint4
- [ ] git rebase를 통한 커밋 관리
- [ ] git push를 통해 내 레포지토리 상태 동기화

### 컨트롤러 레이어 구현

- [ ]  지금까지 구현한 서비스 로직을 활용해 웹 API를 구현하세요.
- 이때 @RequestMapping만 사용해 구현해보세요.

3. 웹 API 요구사항

- [ ]  웹 API의 예외를 전역으로 처리하세요.

4. API 테스트

- [ ] Postman을 활용해 컨트롤러를 테스트 하세요.
    - Postman API 테스트 결과를 다음과 같이 export하여 PR에 첨부해주세요.

### 웹 API 요구사항

1. 사용자 관리

- [ ] 사용자를 등록할 수 있다.
- [ ] 사용자 정보를 수정할 수 있다.
- [ ] 사용자를 삭제할 수 있다.
- [ ] 모든 사용자를 조회할 수 있다.
- [ ] 사용자의 온라인 상태를 업데이트할 수 있다.

2. 권한 관리

- [ ] 사용자는 로그인할 수 있다.

3. 채널 관리

- [ ] 공개 채널을 생성할 수 있다.
- [ ] 비공개 채널을 생성할 수 있다.
- [ ] 공개 채널의 정보를 수정할 수 있다.
- [ ] 채널을 삭제할 수 있다.
- [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.

4. 메시지 관리

- [ ] 메시지를 보낼 수 있다.
- [ ] 메시지를 수정할 수 있다.
- [ ] 메시지를 삭제할 수 있다.
- [ ] 특정 채널의 메시지 목록을 조회할 수 있다.

5. 메시지 수신 정보 관리

- [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
- [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
- [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.

6. 바이너리 파일 다운로드

- [ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.

### 심화 요구사항

1. 정적 리소스 서빙

- [ ]  사용자 목록 조회, BinaryContent 파일 조회 API를 다음의 조건을 만족하도록 수정하세요.
- [ ]  사용자 목록 조회
  - url: /api/user/findAll

- 요청
    - 파라미터, 바디 없음
- 응답
    - ResponseEntity<List<UserDto>>
      ~~~java
          public record UserDto(
              UUID id,
              Instant createdAt,
              Instant updatedAt,
              String username,
              String email,
              UUID profileId,
              Boolean online
          ) {
          }
      ~~~
    - [ ]  BinaryContent 파일 조회
    - url: /api/binaryContent/find
    - 요청
        - 파라미터: binaryContentId
        - 바디 없음
        - 응답: ResponseEntity<BinaryContent>
- [ ]  주어진 static 파일을 활용하여 사용자 목록을 보여주는 화면을 서빙해보세요.

## 미완성 및 개선해야할 부분

### 스프린트4

- [ ] 바이너리 Path -> URL로 변경
- [ ] 채널 조회에 컨트롤러러 인터페이스 부분 잘 동작하나 확인하기

### 여유 있으면 할것

- [ ] DI에 대하여 심층적으로 탐구해보기 [싱글톤, 팩터리메서드, 서비스로케이터, 레지스터리]
- [ ] 전체적인 예외처리 [커스템 예외]
- [ ] 스프링테스트와 통합테스트 공부
- [ ] 페이징 처리에 대해 탐구해보기/순환참조 탐구해보기
- [ ] Protocol Buffer 사용
- [ ] 생산성을 방해하지 않을 정도의 테스트를 작성하는 법 탐구하기
- [ ] 트랜잭션 학습
- [ ] 컨트롤러말고 

### 멘토님 피드백

- [ ] DTO 다형성 삭제 : 명확해야 관리하기가 쉽다
- [x] DTOs 관리하기할때 내부에서 선언하여 관리하는 법 학습
- [x] DTO 네이밍 규칙 반영
  - 컨트롤러 : Request/Response
  - Service 응답 : Info/Result
- [x] UserStatues Dto, 안에 메서드 넣어 놓은 부분 수정
- [x] serialVersionUID 수정
- [x] loadAndSave 수정 : 문제 - 같은 동작을 하는 메서드2개 정의
- [x] Repository는
  - 조회 (Read)
  - 저장 (Create/Update)
  - 삭제 (Delete)
- [ ] 최대한 도메인 객체를 그대로 사용하다가 마지막에 DTO로 for 유지보수
- [ ] 이미지 파일을 바이트 자체로 저장하는 방향, 조회는 path를 반환하더라도
- [ ] application.yaml파일 수정


