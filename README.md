# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

## JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이

### JavaApplication

- 직접 객체 생성 및 주입: 개발자가 new 키워드를 사용하여 직접 객체를 생성하고 생성자를 통해 의존성을 주입.
- 수동 의존성 관리: 개발자가 직접 모든 의존성을 관리해서 코드가 복잡함.

### DiscodeitApplication

- IoC 컨테이너 사용: 스프링의 IoC(Inversion of Control) 컨테이너를 사용하여 Service를 초기화.
- 자동 의존성 관리: @SpringBootApplication 어노테이션을 사용하면 스프링이 자동으로 필요한 객체를 Bean으로 등록하고, 의존성을 관리해서 코드가 간결함..