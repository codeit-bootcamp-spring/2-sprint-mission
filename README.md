# pprt1-sprint2 요구사항

## 기본 요구사항

### File IO를 통한 데이터 영속화

- [x]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
- [x]  클래스 패키지명: com.sprint.mission.discodeit.service.file
- [x]  클래스 네이밍 규칙: File[인터페이스 이름]
- [x]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
  - [x] User
  - [x] Channel
  - [x] Message
- [x]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

### 서비스 구현체 분석

- [x] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요.
- [x] "비즈니스 로직"과 관련된 코드를 식별해보세요.
- [x] "저장 로직"과 관련된 코드를 식별해보세요.

### 레포지토리 설계 및 구현

- [x] "저장 로직"과 관련된 기능을 도메인 모델 별 인터페이스로 선언하세요.
- [x] 인터페이스 패키지명: com.sprint.mission.discodeit.repository
- [x] 인터페이스 네이밍 규칙: [도메인 모델 이름]Repository
- [x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.
- [x] 클래스 패키지명: com.sprint.mission.discodeit.repository.jcf
- [x] 클래스 네이밍 규칙: JCF[인터페이스 이름]
- [x] 기존에 구현한 JCF*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.
- [x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.
- [x] 클래스 패키지명: com.sprint.mission.discodeit.repository.file
- [x] 클래스 네이밍 규칙: File[인터페이스 이름]
  - [x] User
  - [x] Channel
  - [x] Message
- [x] 기존에 구현한 File*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

## 심화 요구 사항

관심사 분리를 통한 레이어 간 의존성 주입

- 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
- [ ] 클래스 패키지명: com.sprint.mission.discodeit.service.basic
- [ ] 클래스 네이밍 규칙: Basic[인터페이스 이름]
- [ ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
- [ ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
- [ ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
- [ ] Basic*Service 구현체를 활용하여 테스트해보세요.
- [ ] JCF*Repository 구현체를 활용하여 테스트해보세요.
- [ ] File*Repository 구현체를 활용하여 테스트해보세요.
- [ ] 이전에 작성했던 코드(JCF*Service 또는 File*Service)와 비교해 어떤 차이가 있는지 정리해보세요

# 미완성 및 개선해야할 부분

- "[]" 부분은 추후 공부를 위해 관련된 키워드의 모음을 적은 것 입니다.
- [ ] DI 구현 보완: [싱글톤, 팩터리메서드, 서비스로케이터, 레지스터리]
- [ ] OutputView - createUserName에서 id 비교하는 부분 equals로 수정[equals&hashCode]
- [ ] Application 사용자 입력 처리부분 수정[조건문 다형성으로 추상화하기, 완전한 커맨드 패턴 적용해보기]
- [ ] InputView Scanner 자원할당 방식 수정필요[try-with-resource]
- [ ] 도메인별 id 객체로 변경[플러그인, 식별자 필드]
    - 도메인 모델의 Id가 UUID에 의존하지 않도록 수정
- [ ] 전체적인 예외처리[커스템 예외]
    - [ ] 예외 발생시 입력을 다시 받거나 따로 처리해주기
- [ ] 응용서비스에 들어있는 기능 도메인 서비스로 옮길 수 있나 생각해보기[원격파사드]
- [ ] 코드잇 모범답안의 예외처리 부분 추가하기
- [ ] 파일 서비스들의 테스트 저장소끼리 격리 필요
- [ ] saveObject를 안하면 동기화 문제 발생하는 것 어떻게 해결할지 생각해보기
- [ ] 레포지터리의 예외처리부분 서비스에만 할당하기
- [ ] null 반환부분 Optional이나 null객체로 바꾸기

# 멘토님 피드백
- [x] findBean 한번만 선언
- [ ] 커맨드패턴 보완
- [x] Unix TimeStamp ms로 수정
- [ ] updatedAt 메서드 이름 수정
- [ ] toDto 정적 팩터리 메서드로 보완하기
- [ ] Scanner 사용시 Test 격리성 높이기
- [ ] 변경이나 생성 후 바로 객체 반환하는 코드로 수정하기
- [ ] 테스트 명칭 조금 더 명확하게[~하면 ~가 출력된다]