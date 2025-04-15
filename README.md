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

위의 코드처럼 직접 객체를 넘겨주어 IoC와 DI를 수동으로 구현할 수 있지만,
Spring을 사용하면 이러한 의존성 주입을 자동으로 관리해준다.

```
@SpringBootApplication
...

ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
BasicUserService userService = context.getBean(BasicUserService.class);
```

@SpirngBootApplication의 내부 어노테이션들이 실행되며 컴포넌트 스캔과 자동 설정이 활성화된다. 이 과정에서 @Service, @Repository
등을 스캔하여 Bean으로 등록하게 되며, 실제 의존성 주입은 Spring의 DI 컨테이너가 관리한다.
즉, **어노테이션의 순서와 관계없이** Spring이 의존성 관계를 파악하고, @Repository를 초기화한 후 @Service에 자동으로 주입하는 방식이다.

따라서 개발자는 직접 의존성 주입을 하지 않아도 되고, Spring의 자동 의존성 주입으로 코드가 간결해지며 클래스 간에 결합도가 낮아져 유지보수성이 좋아진다.

## Sprint6 - ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보

| 엔티티 관계           	                 | 다중성 	    | 방향성                        	            | 부모-자식 관계                     	              | 연관관계의 주인 	          |
|------------------------------------|----------|-----------------------------------------|---------------------------------------------|---------------------|
| users:binary_contents 	            | 1:1    	 | users->binary_contents 단방향 	            | 부모: users, 자식: binary_contents 	            | users           	   |
| messages:users        	            | N:1    	 | messages->users 단방향                     | 부모: users, 자식: messages 	                   | messages 	          |
| messages:channels	                 | N:1 	    | messages->channels 단방향 	                | 부모: channels, 자식: messages	                 | messages 	          |
| messages:message_attachment        | 1:N	     | message_attachment->messages 단방향 	      | 부모: messages, 자식: message_attachment	       | message_attachment	 |
| message_attachment:binary_contents | 1:1 	    | message_attachment->binary_contents 단방향 | 부모: binary_contents, 자식: message_attachment | message_attachment  |
| user_statuses:users	               | 1:1 	    | user_statuses->users 단방향                | 부모: users, 자식: user_statuses                | user_statuses       |
| read_statuses:users	               | N:1 	    | read_statuses->users 단방향                | 부모: users, 자식: read_statuses                | read_statuses       |
| read_statuses:channels	            | N:1 	    | read_statuses->channels 단방향	            | 부모: channels, 자식: read_statuses             | read_statuses       |