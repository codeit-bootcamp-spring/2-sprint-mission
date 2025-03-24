# 2-sprint-mission
2기 스프린트 미션 제출 리포지토리입니다.

## JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식 차이
### IoC Container
스프링 미사용 - 객체 생성하려면 new 명령어로 직접 생성해야한다.   
스프링 사용 - IoC Container에서 spring이 객체를 관리한다.

### Dependency Injection
스프링 미사용 - 수동으로 의존성을 주입해야한다.   
스프링 사용 - spring이 자동으로 의존성을 주입한다.

### Bean
스프링 미사용 - spring bean이라는 개념은 없으며, 단순한 클래스 인스턴스이다.  
스프링 사용 - spring에서 관리되는 객체를 의미하고, 일반적으로 spring container에 등록된 객체를 의미한다.
