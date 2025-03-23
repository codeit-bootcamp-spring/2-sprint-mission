# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

## JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이

### JavaApplication (직접 생성 방식)
- 각 Service와 Repository를 개발자가 직접 `new`로 생성하고 조립함
- 객체 간 의존성을 수동으로 연결해야 하므로, 클래스 간 결합도가 높아지고 유지보수가 어려움

### DiscodeitApplication (Spring 방식)
- Spring의 IoC 컨테이너가 모든 객체(Bean)를 대신 생성하고 관리
- 필요한 Service는 `context.getBean()` 또는 `@Autowired` 등을 통해 **Dependency Injection**됨
- 설정값에 따라 Repository 구현체를 자동으로 선택하고 주입함

### 핵심 개념 정리
- IoC Container: 객체의 생성과 생명주기를 관리하는 스프링의 중심 구조
- Dependency Injection: 필요한 객체를 직접 생성하지 않고 외부에서 주입받는 방식
- Bean: Spring IoC 컨테이너가 관리하는 객체. 설정에 따라 자동으로 등록되고 주입 가능
