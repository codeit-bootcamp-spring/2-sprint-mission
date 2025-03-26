# 2-sprint-mission

2기 스프린트 미션 제출 리포지토리입니다.

## JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이

### JavaApplication

- 직접 객체를 생성하는 주입하는 방식으로 Service를 초기화합니다.
- new 키워드를 통해 직접 Service와 Repository 객체를 생성하고, 생성자를 통해 직접 의존성을 주입합니다.
- 개발자가 직접 의존성을 관리해야하는 불편함이 있습니다.

### DiscodeitApplication

- 스프링의 IoC 컨테이너를 사용하여 Service를 초기화합니다.
- SpringApllication.run() 메서드를 호출하면 스프링 IoC 컨테이너를 생성되고, @SpringBootApplication을 기준으로 컴포넌트 스캔을 하여 필요한 객체를 자동으로 Bean으로
  등록합니다.
- context.getBean() 메서드를 통해 Spring이 관리하는 Bean을 가져와 사용합니다.
- 스프링이 의존성을 자동으로 관리하므로 개발자가 직접 객체를 생성하고 의존성을 관리할 필요가 없습니다.