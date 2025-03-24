## Spring 핵심 개념 이해하기

- [x] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.

    - IoC Container
    - Dependency Injection
    - Bean

-> JavaApplication에선 Basic*Service이 의존하는 객체를 개발자가 직접 생성해줬어야 했다.

```
// UserRepository의 구현체인 FileUserRepository를 사용하기 위해 직접 초기화를 해주고, 매개변수로 넘겨줘야 함.
UserRepository userRepository = new FileUserRepository();
UserService userService = new BasicUserService(userRepository); 
``` 

위의 코드처럼 직접 객체를 넘겨주어 IoC와 DI를 수동으로 구현할 수 있지만, Spring을 사용하면 이러한 의존성 주입을 자동으로 관리해준다.

```
@SpringBootApplication
...

ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
BasicUserService userService = context.getBean(BasicUserService.class);
```

@SpirngBootApplication의 내부 어노테이션들이 실행되며 컴포넌트 스캔과 자동 설정이 활성화된다. 이 과정에서 @Service, @Repository
등을 스캔하여 Bean으로 등록해야 한다는 것을 인지한 후 의존성 관계를 파악하여 @Repository를 초기화해준 다음 @Service를 초기화하면서
@Repository를 자동으로 주입해준다.

따라서 개발자는 직접 의존성 주입을 하지 않아도 되고, Spring의 자동 의존성 주입으로 코드가 간결해지며 클래스 간에 결합도가 낮아져 유지보수성이 좋아진다.
