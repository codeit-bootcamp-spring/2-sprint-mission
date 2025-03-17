# 프로젝트 마일스톤

## Java 프로젝트를 Spring 프로젝트로 마이그레이션
## 의존성 관리를 IoC Container에 위임하도록 리팩토링
## 비즈니스 로직 고도화

### 요구사항

- Dependency를 추가합니다.
  - Lombok
  - Spring Web

#### Bean 선언 및 테스트 

- File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
- Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
- JavaApplication에서 테스트했던 코드를 DiscodeitApplication에서 테스트해보세요.
  - JavaApplication 의 main 메소드를 제외한 모든 메소드를 DiscodeitApplication클래스로 복사하세요.
  - JavaApplication의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.

