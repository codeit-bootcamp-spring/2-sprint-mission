# 2-sprint-mission

2기 스프린트 미션 제출 리포지토리입니다.

# 스프린트 미션1 - 프로젝트 마일스톤

### 기본

- [x] 프로젝트 초기화 (Java, Gradle)
- [x] 도메인 모델 구현
- [x] 서비스 인터페이스 설계 및 구현체 구현
- [x] 각 도메인 모델별 CRUD
- [x] JCFx메모리 기반

### 심화

- [ ] 의존성 주입

# 프로젝트 세부 요구사항

## 도메인 모델링

- 패키지명: com.sprint.mission.discodeit.entity
- 도메인 모델 정의
- 공통
- [x] id: 객체를 식별하기 위한 id로 UUID 타입으로 선언합니다.
- [x] createdAt, updatedAt: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로 나타내기 위한 필드로 Long 타입으로 선언합니다.
- [x] User, Channel, Message

- 생성자
- [x] id는 생성자에서 초기화하세요.
- [x] createdAt는 생성자에서 초기화하세요.
- [x] id, createdAt, updatedAt을 제외한 필드는 생성자의 파라미터를 통해 초기화하세요.

- 메소드
- [x] 각 필드를 반환하는 Getter 함수를 정의하세요.
- [x] 필드를 수정하는 update 함수를 정의하세요.

## 서비스 설계 및 구현

- 도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
- 인터페이스 패키지명: com.sprint.mission.discodeit.service
- 인터페이스 네이밍 규칙: [도메인 모델 이름]Service

- 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
- 클래스 패키지명: com.sprint.mission.discodeit.service.jcf
    - 클래스 네이밍 규칙: JCF[인터페이스 이름]
    - Java Collections Framework를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언하고 생성자에서 초기화하세요.
    - data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

- [x] User
    - [x] 생성
    - [x] 읽기
    - [x] 모두 읽기
    - [x] 수정
    - [x] 삭제
- [x] Channel
    - [x] 생성
    - [x] 읽기
    - [x] 모두 읽기
    - [x] 수정
    - [x] 삭제
- [x] Message
    - [x] 생성
    - [x] 읽기
    - [x] 모두 읽기
    - [x] 수정
    - [x] 삭제

## 메인 클래스 구현

- 메인 메소드가 선언된 JavaApplication 클래스를 선언하고, 도메인 별 서비스 구현체를 테스트해보세요.
- 등록
- 조회(단건, 다건)
- 수정
- 수정된 데이터 조회
- 삭제
- 조회를 통해 삭제되었는지 확인

## 기본 요구사항 커밋 태그

- [x] 여기까지 진행 후 반드시 커밋해주세요. 그리고 sprint1-basic 태그를 생성해주세요.

# 심화 요구사항

## 서비스 간 의존성 주입: DI와 Composition

- 도메인 모델 간 관계를 고려해서 검증하는 로직을 추가하고, 테스트해보세요.
- 힌트: Message를 생성할 때 연관된 도메인 모델 데이터 확인하기

## 기능 구현 목록 

1. 채널 채팅입력 출력 예시
~~~
  "안녕하세요 코드잇2기 서버입니다.",
  "—---------------------------------------------------------",
  " 코드잇 2기  | general",
  "—---------------------------------------------------------",
  "# general |                                      | # 황지환",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번",
  "# 현재 채널을 어떤 이름으로 변경하시겠습니까? : ",
  "—---------------------------------------------------------",
  " 코드잇 2기  | 7팀",
  "—---------------------------------------------------------",
  "# 7팀      |                                      | # 황지환",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번",
  "# 친구 이메일 : ",
  "—---------------------------------------------------------",
  " 코드잇 2기  | 7팀",
  "—---------------------------------------------------------",
  "# 7팀      |                                      | # 황지환",
  "          |                                      |   박지환",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번",
  "# 메세지 입력 : ",
  "—---------------------------------------------------------",
  " 코드잇 2기  | 7팀",
  "—---------------------------------------------------------",
  "# 7팀      | 안녕하세요 황지환입니다.                    | # 황지환",
  "          |                                      |   박지환",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번",
  "# 생성할 채널의 이름을 설정해주세요 : ",
  "—---------------------------------------------------------",
  " 코드잇 2기  | 스터디",
  "—---------------------------------------------------------",
  " general |                                      | # 황지환",
  "# 스터디   |                                      |",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번",
  "# 이동할 채널을 선택해주세요",
  "- 7팀 : 1번",
  "—---------------------------------------------------------",
  " 코드잇 2기  | 7팀",
  "—---------------------------------------------------------",
  "# 7팀      | 안녕하세요 황지환입니다.                    | # 황지환",
  "          |                                      |   박지환",
  "—---------------------------------------------------------",
  "# 하고 싶은 기능 선댁",
  "- 다른 채널 생성 : 1번",
  "- 현재 채널에 유저 등록 : 2번",
  "- 현재 채널 이름변경 : 3번",
  "- 현재 채널에 친구 추가 : 4번",
  "- 현재 채널에 메세지 입력 : 5번",
  "- 다른 채널 이동 : 6번",
  "- 종료 : 7번"
~~~

## 기능 목록
1. 초기 채널 생성 기능
- [x] 유저등록(황지환, 박지환)
- [x] 유저:황지환으로 채널 만들기 
- [x] general 이름의 채널 생성
- [x] 채널 조회 뷰 생성(채널, 메세지, 유저)
- [x] 7가지 기능 선택뷰 생성
2. 채널이름 변경
- [x] 채널 이름 업데이트 기능
- [x] 채널 조회 뷰에 반영
3. 채널에 유저 초대
- [x] 이메일 입력
- [x] 채널에 속한 유저에 추가
4. 채널 메세지 입력 기능
- [x] 유저가 메세지 입력
- [x] 채널에서 바로 조회
5. 채널 추가 생성 
- [x] 생성할 채널 이름 입력 기능
- [x] 채널 생성기능
- [x] 추가된 채널로 이동(+메세지 불러오기) 
- [ ] 채널, 유저, 메세지 영역들 분리해서 출력하는 걸로 수정
6. 다른 채널로 이동
- [ ] 이동할 채널 이름 선택
- [ ] 채널 이동(+메시지 불러오기)

## 추가 요구사항
- [ ] 전체적인 예외처리
- [x] 서비스, 레포지토리 싱글톤으로 변경
- [ ] 도메인별 id 객체로 변경
    - 도메인 모델의 Id가 UUID에 의존하지 않도록 수정
- [x] CRUD구현 레포지토리로 분리
- [ ] 회원가입된 유저가 다시 들어왔을떄 유저가 속한 채널 메세지 내용 업로드
