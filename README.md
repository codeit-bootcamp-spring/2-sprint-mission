# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

# 스프린트 미션1 - 프로젝트 마일스톤
### 기본
- [x] 프로젝트 초기화 (Java, Gradle)
- [x] 도메인 모델 구현
- [ ] 서비스 인터페이스 설계 및 구현체 구현
- [ ] 각 도메인 모델별 CRUD
- [ ] JCFx메모리 기반

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
-  도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
  - 인터페이스 패키지명: com.sprint.mission.discodeit.service
  - [ ] 인터페이스 네이밍 규칙: [도메인 모델 이름]Service


-  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
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
- [ ] 등록
- [ ] 조회(단건, 다건)
- [ ] 수정
- [ ] 수정된 데이터 조회
- [ ] 삭제
- [ ] 조회를 통해 삭제되었는지 확인

## 기본 요구사항 커밋 태그
- [ ] 여기까지 진행 후 반드시 커밋해주세요. 그리고 sprint1-basic 태그를 생성해주세요.
  
# 심화 요구사항
## 서비스 간 의존성 주입
- [ ] 도메인 모델 간 관계를 고려해서 검증하는 로직을 추가하고, 테스트해보세요.
  힌트: Message를 생성할 때 연관된 도메인 모델 데이터 확인하기