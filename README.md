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
- [x] 클래스 패키지명: com.sprint.mission.discodeit.service.basic
- [x] 클래스 네이밍 규칙: Basic[인터페이스 이름]
- [x] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
- [x] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
- [x] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
- [x] Basic*Service 구현체를 활용하여 테스트해보세요.
  - [x] JCF*Repository 구현체를 활용하여 테스트해보세요.
  - [x] File*Repository 구현체를 활용하여 테스트해보세요.
- [x] 이전에 작성했던 코드(JCF*Service 또는 File*Service)와 비교해 어떤 차이가 있는지 정리해보세요
~~~java
// 참고 하겠습니다.
public class JavaApplication {
    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
~~~

# 미완성 및 개선해야할 부분

## 현재 미션
- [ ] null 반환부분 Optional이나 null객체로 바꾸기[Optional, null객체]
- [x] 파일 서비스들의 테스트 저장소끼리 격리 필요[테스트 격리성]
- [ ] protocol Buffer 사용

## 공통 미션
- [ ] DI 구현 보완: [싱글톤, 팩터리메서드, 서비스로케이터, 레지스터리]
- [ ] OutputView - createUserName에서 id 비교하는 부분 equals로 수정[equals&hashCode]
- [ ] Application 사용자 입력 처리부분 수정[조건문 다형성으로 추상화하기, 완전한 커맨드 패턴 적용해보기]
- [ ] 도메인별 id 객체로 변경[플러그인, 식별자 필드]
- [ ] 전체적인 예외처리[커스템 예외]
- [ ] 응용서비스에 들어있는 기능 도메인 서비스로 옮길 수 있나 생각해보기[원격파사드]
- [ ] saveObject를 안하면 파일 서비스 동기화 문제 발생한다면 어떻게 해결할지 생각해보기

# 멘토님 피드백
- [x] findBean 한번만 선언
- [ ] Enum 리펙토링부분 커맨드패턴으로 보완
- [x] Unix TimeStamp ms로 수정
- [ ] updatedAt 메서드 이름 수정
- [ ] toDto 정적 팩터리 메서드로 보완하기[정적 팩터리 메서드]
- [ ] Scanner등 자원 사용시 Test 격리성 높이기[try-with-resource, 테스트 격리성]
- [ ] 테스트 명칭 조금 더 명확하게[~하면 ~가 출력된다]