# Part1-sprint3

## 기본 요구사항

1. git 브랜치 관리

- [x] 브랜치 변경 : part1-황지환-sprint3
- [x] git rebase를 통한 커밋 관리
- [x] git push를 통해 내 레포지토리 상태 동기화

2. Spring 프로젝트 초기화

- [ ] Spring Initializr를 통해 zip 파일을 다운로드하세요.
    - [x] 빌드 시스템은 Gradle - Groovy를 사용합니다.
    - [x] 언어는 Java 17를 사용합니다.
    - [x] Spring Boot의 버전은 3.4.0입니다.
    - [x] GroupId는 com.sprint.mission입니다.
    - [x] ArtifactId와 Name은 discodeit입니다.
    - [x] packaging 형식은 Jar입니다
    - [x] Dependency를 추가합니다.
        - [x] Lombok
        - [x] Spring Web
- [x] zip 파일을 압축해제하고 원래 진행 중이던 프로젝트에 붙여넣기하세요. 일부 파일은 덮어쓰기할 수 있습니다.
- [x] application.properties 파일을 yaml 형식으로 변경하세요.
- [x] DiscodeitApplication의 main 메서드를 실행하고 로그를 확인해보세요.


3. Bean 선언 및 테스트

- [x] File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
- [x] Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
- [x] JavaApplication에서 테스트했던 코드를 DiscodeitApplication에서 테스트해보세요.
    - [x]  JavaApplication 의 main 메소드를 제외한 모든 메소드를 DiscodeitApplication클래스로 복사하세요.
    - [x]  JavaApplication의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.

~~~java
  // JavaApplication
public static void main(String[]args){
        // 레포지토리 초기화
        // ...
        // 서비스 초기화
        UserService userService=new BasicUserService(userRepository);
        ChannelService channelService=new BasicChannelService(channelRepository);
        MessageService messageService=new BasicMessageService(messageRepository,channelRepository,userRepository);

        // ...
        }

// DiscodeitApplication
public static void main(String[]args){
        ConfigurableApplicationContext context=SpringApplication.run(DiscodeitApplication.class,args);
        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        // ...
        }
  ~~~

- [x]  JavaApplication의 main 메소드의 셋업, 테스트 부분의 코드를 DiscodeitApplication클래스로 복사하세요.

  ~~~java
  public static void main(String[]args){
        // ...
        // 셋업
        User user=setupUser(userService);
        Channel channel=setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService,channel,user);
        }
  ~~~

4. Spring 핵심 개념 이해하기[다음주 월요일 예정]

- [ ] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
    - IoC Container
    - Dependency Injection
    - Bean

5. Lombok 적용

- [x] 도메인 모델의 getter 메소드를 @Getter로 대체해보세요.
- [x] Basic*Service의 생성자를 @RequiredArgsConstructor로 대체해보세요.

## 기능 요구사항

### 대표적인 기능 구현 흐름 정리

#### UserService 고도화

- [x] 유저 등록시 선택적으로 프로필 이미지 등록(UserService 고도화)
    - 프로필 이미지 입력(url or FilePath)/시간이 된다면 실제 이미지 업로드 해보기
    - 이미지 경로 등록
    - 등록된 유저정보와 함께 이미지 id와 경로 반환
- [ ] 유저 프로필이미지 변경 기능
    - 프로필 이미지 입력(url or FilePath)
    - 입력된 이미지 경로로 변경
    - 등록된 유저정보와 함께 이미지 id와 경로 반환

#### AuthService 구현

- [ ] 로그인 기능
    - username, password 입력
    - 로그인시 유저의 UserStatus 로그인으로 갱신
    - 로그인한 유저의 id, 이름, 프로필 ID 반환

#### ChannelService 고도화

- [ ] 채널 private, public 메서드 구분
    - [ ] 채널 생성
        - [ ] private 채널 생성
            - private 상태와 유저 id 전달
            - private에 참여하는 유저 id와 유저별 readStatus 채널에 저장
        - [ ] public 채널 생성[기존로직 탐구 필요]
            - public 매개변수 전달
            - public 생성
    - [ ] 채널 조회 - find(), findALL()
        - [ ] public 조회 및 반환
        - [ ] 유저 private한 채널 반환
            - 본인 id 입력값 받기
            - 채널 private 중에 본인이 속한 채널 조회

#### MessageService 고도화

- [ ] 메세지 첨부파일 등록기능
    - 첨부파일 경로 입력
    - 메세지와 함께 첨부파일 저장
    - 메세지 id와 함께 반환

#### ReadStatusService 구현

- [ ] 채널 메세지 읽음 확인
    - [ ] 유저가 마지막으로 메세지를 읽은 시점 기록
        - 시점 : 해당 채널에 나갔을떄 기록
    - [ ] 유저가 해당채널에 다시 들어왔을떄 위에 시점 반환

#### UserStatusService 고도화

- [ ] 사용자의 로그인 상태 확인 : 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
    - [ ] 마지막 접속 시간 기록
        - 로그인 했으면 로그인 시점이 마지막 접속시간
        - 로그아웃을 했으면 아웃한 시점이 마지막 접속시간
    - [ ] 마지막 접속 시간과 현재 시간 비교(로그아웃한 경우)
        - 마지막 접속 시간과 현재시간 차이가 5분 이하면 로그인(5분안에 다시 로그인한경우)
        - 마지막 접속 시간과 현재시간 차이가 5분 이상이면 로그아웃
    - [ ] 로그인 상태(로그인/로그아웃)

## 기능 구현 세부사항

### 도메인 모델 수정

1. 시간 타입 변경하기

- [x] 시간을 다루는 필드의 타입은 Instant로 통일합니다.
    - 기존에 사용하던 Long보다 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.

2. 새로운 도메인 추가하기

- [ ] 공통: 앞서 정의한 도메인 모델과 동일하게 공통 필드(id, createdAt, updatedAt)를 포함합니다.


- [ ] ReadStatus(userId, channelId)
    - 사용자가 채널 별 **마지막으로 메시지를 읽은 시간**을 표현하는 도메인 모델입니다. 사용자별 각 채널에 **읽지 않은 메시지를 확인**하기 위해 활용합니다.


- [x]  UserStatus(userId)
- 사용자 별 **마지막으로 확인된 접속 시간**을 표현하는 도메인 모델입니다. **사용자의 온라인 상태를 확인**하기 위해 활용합니다.
- [ ] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
    - 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.


- [x] BinaryContent(profileId)
    - 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. **사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장**하기 위해 활용합니다.
    - [x] 수정 불가능한 도메인 모델로 간주합니다. 따라서 updatedAt 필드는 정의하지 않습니다.
    - [x] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.


- [ ]  각 도메인 모델 별 레포지토리 인터페이스를 선언하세요.
- 레포지토리 아직 구현하지마세요. 이어지는 서비스 고도화 요구사항에 따라 레포지토리 인터페이스에 메소드가 추가될 수 있어요.

### *Service 고도화

#### UserService 고도화

1. create

- [x] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
- [x] DTO를 활용해 파라미터를 그룹화합니다.
    - 유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
- [x] username과 email은 다른 유저와 같으면 안됩니다.
- [x] UserStatus를 같이 생성합니다.

2. find, findAll

- DTO를 활용하여
- [x] 사용자의 온라인 상태 정보를 같이 포함하세요.
- [x] 패스워드 정보는 제외하세요.

3. update

- [x] 선택적으로 프로필 이미지를 대체할 수 있습니다.
- [x] DTO를 활용해 파라미터를 그룹화합니다.
- 수정 대상 객체의 id 파라미터, 수정할 값 파라미터

4. delete

- [ ] 관련된 도메인도 같이 삭제합니다.
    - BinaryContent(프로필), UserStatus

#### AuthService 구현

1. login

- [ ] username, password과 일치하는 유저가 있는지 확인합니다.
    - [ ] 일치하는 유저가 있는 경우: 유저 정보 반환
    - [ ] 일치하는 유저가 없는 경우: 예외 발생
- [ ] DTO를 활용해 파라미터를 그룹화합니다.

#### ChannelService 고도화

1. create

- PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
  - [ ] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
- PRIVATE 채널을 생성할 때:
  - [ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
  - [ ] name과 description 속성은 생략합니다.
- PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.

2. find

- DTO를 활용하여:
    - [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
    - [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.

3. findAll

- DTO를 활용하여:
    - [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
    - [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
- [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
- [ ] PUBLIC 채널 목록은 전체 조회합니다.
- [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.

4. update

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
    - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
- [ ] PRIVATE 채널은 수정할 수 없습니다.

5. delete

- [ ] 관련된 도메인도 같이 삭제합니다.
    - Message, ReadStatus

#### MessageService 고도화

1. create

- [ ] 선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다.
- [ ] DTO를 활용해 파라미터를 그룹화합니다.

2. findAll

- [ ] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId

3. update

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
    - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터

4. delete

- [ ] 관련된 도메인도 같이 삭제합니다.
  첨부파일(BinaryContent)

#### ReadStatusService 구현

1. create

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
- [ ] 관련된 Channel이나 User가 존재하지 않으면 예외를 발생시킵니다.
- [ ] 같은 Channel과 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.

2. find

- [ ] id로 조회합니다.

3. findAllByUserId

- [ ] userId를 조건으로 조회합니다.

4. update

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
    - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터

5. delete

- [ ] id로 삭제합니다.

#### UserStatusService 고도화

1. create

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
- [ ] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
- [ ] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.

2. find

- [ ] id로 조회합니다.

3. findAll

- [ ] 모든 객체를 조회합니다.

4. update

- [ ] DTO를 활용해 파라미터를 그룹화합니다.
- 수정 대상 객체의 id 파라미터, 수정할 값 파라미터

5. updateByUserId

- [ ] userId 로 특정 User의 객체를 업데이트합니다.

6. delete

- [ ] id로 삭제합니다.

#### BinaryContentService 구현

1. create

- [x] DTO를 활용해 파라미터를 그룹화합니다.

2. find

- [ ] id로 조회합니다.

3. findAllByIdIn

- [ ] id 목록으로 조회합니다.

4. delete

- [ ] id로 삭제합니다.

##### 새로운 도메인 Repository 구현체 구현

- [ ]  지금까지 인터페이스로 설계한 각각의 Repository를 JCF, File로 각각 구현하세요.

## 심화 요구사항

- Bean 다루기
    - [ ]  Repository 구현체 중에 어떤 구현체를 Bean으로 등록할지 Java 코드의 변경 없이 application.yaml 설정 값을 통해 제어해보세요.
    - [ ] discodeit.repository.type 설정값에 따라 Repository 구현체가 정해집니다.
        - [ ] 값이 jcf 이거나 없으면 JCF*Repository 구현체가 Bean으로 등록되어야 합니다.
        - [ ] 값이 file 이면 File*Repository 구현체가 Bean으로 등록되어야 합니다.
  ~~~yaml
  # application.yaml
  discodeit:
      repository: 
          type: jcf   # jcf | file
  ~~~ 

- [ ]  File*Repository 구현체의 파일을 저장할 디렉토리 경로를 application.yaml 설정 값을 통해 제어해보세요.
  ~~~yaml
  # application.yaml
  discodeit:
      repository: 
          type: jcf   # jcf | file
          file-directory: .discodeit
  ~~~

## 미완성 및 개선해야할 부분

### 파일 저장소 미션

- [ ] Protocol Buffer 사용

### 여유 있으면 할것

- [ ] DI에 대하여 심층적으로 탐구해보기 [싱글톤, 팩터리메서드, 서비스로케이터, 레지스터리]
- [ ] 전체적인 예외처리 [커스템 예외]
- [ ] File*Repository에서 loadSave 중복되는 부분 개선하기[일반화, 템플릿 메서드 패턴, 전략패턴]

### 1차 멘토님 피드백

- [x] Applicaiont 조건절 Enum 리펙토링 커맨드패턴으로 전환
- [x] Unix TimeStamp ms로 수정
- [x] Scanner등 자원 사용시 Test 격리성 높이기[try-with-resource, 테스트 격리성]
- [x] toDto 정적 팩터리 메서드로 보완하기[정적 팩터리 메서드]

### 2차 멘토님 피드백

- [x] file 저장 경로 수정 : src 폴더 아래에 소스코드만 위치하게하고 폴더 파일들은 분리한다. 깃허브에 올릴떄는 .gitignore 사용하기
- [x] Enum 탐구해보기 : 필드에서 사용할때, 바꿔끼울 수 있을떄(타입, 다형성), 서로 연관된 상수들
- [x] updateTimeStamp메서드 이름 수정하기
- [x] 순환참조 탐구하기 : 같은 서비스 레이어에서 필드에 서비스보다는 레포지토지 참조
- [x] 중복이메일 예외 처리로직 중 filter + findFirst부분 anyMatch로 수정하기
- [x] JCF 레포지토리 save이후 null 체크부분 수정하기
- [x] 테스트에 대해서 좀 더 탐구하기 :
    - 객체 초기화 : @BeforeEach로 테스트마다 매번 객체 초기화하는방식, 객체를 생성해 놓고 내용물만 초기화 하는방식
- [ ] 페이징 처리에 대해 탐구해보기
- [ ] 테스트 하기 까다로운것 것(파일 경로 상수)에 대한 테스트 진행하기
    - [ ] 파일저장소 Path 개발환경/운영환경 분리해서 운영할 방법 찾기
