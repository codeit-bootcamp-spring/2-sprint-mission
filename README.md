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

# Sprint6

## ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보

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

## DTO 적극 도입하기

### Entity를 Controller까지 노출했을 때 발생할 수 있는 문제점

- Entity가 변경되면 API 응답도 같이 변경되어야 하므로, 내부 설계를 외부에 노출시키는 위험이 있고 유지보수성이 떨어진다.
- 대부분의 프로덕션 환경에서는 성능을 위해 OSIV를 false로 설정한다. -> Controller에서 LAZY 필드에 접근하면
  `LazyInitializationException`이 발생할 수 있다.
- JPA의 양방향 연관관계를 가진 엔터티를 JOSN으로 직렬화할 때, 무한 순환 참조가 발생할 수 있어 StackOverflowError가 발생한다.
- Entity에 민감한 정보가 포함되어 있으면 그대로 외부로 노출될 수 있다.

### DTO 사용 이점

- 필요한 필드만 포함된 응답 객체를 정의할 수 있어, client에 일관된 데이터를 제공한다.
- Entity 구조가 변경되어도 DTO는 API 스펙을 유지할 수 있어, 변경 범위가 제한된다.
- 노출할 필요 없는 민감한 정보를 쉽게 제외할 수 있다.
- 트랜잭션 범위 밖에서 Lazy 필드에 접근할 일이 줄어들어, `LazyInitializationException`을 방지할 수 있다.
- Entity 간의 복잡한 연관관계로부터 독립적인 구조를 구성할 수 있어, 직렬화 이슈를 피할 수 있다.

## 오프셋 페이지네이션과 커서 페이지네이션 방식의 차이


### 오프셋 페이지네이션 (Offset Pagination)


: OFFSET과 LIMIT를 사용해 특정 위치에서부터 데이터를 일정 개수만큼 조회한다. 
<br>-> ex: 2번째 페이지의 10개 데이터를 조회해야 한다면 `OFFSET 10 LIMIT 10`으로 사용한다.
<br>✔️ `OFFSET`: 건너뛸 데이터의 개수, `LIMIT`: 가져올 데이터 개수 

- 장점
1. 페이지 번호와 페이지 크기만 알면 조회가 가능하다.
2. 전체 데이터 개수, 전체 페이지 수 등을 쉽게 알 수 있어 UI에 유리하다.
3. Spring Data JPA의 Pageable과 Page<T>로 쉽게 구현할 수 있다.

- 단점  
1. 데이터가 많아질수록 OFFSET 비용이 증가해 성능이 나빠진다.
2. 중간에 데이터가 삽입되거나 삭제되면 페이지 경계가 흔들려 중복되거나 누락될 수 있다.

###  커서 페이지네이션 (Cursor Pagination)


: 이전 페이지의 마지막 항목(ex: ID, createdAt 등)을 커서로 삼아 그 이후(or 이전)의 데이터를 조회한다.
<br>-> ex: "createdAt < 마지막 createdAt" 조건으로 다음 데이터를 가져온다.

- 장점
1. 항상 이전 항목 기준으로 다음 항목을 가져오기 때문에 성능이 일정하다.
2. 인덱스를 활용해 빠른 조회가 가능하고, 데이터가 많아도 부담이 적다.
3. 데이터 삽입/삭제에도 안정적으로 동작하여 일관된 결과를 보여준다.

- 단점
1. 커서를 client가 기억하고 있어야 하며, 서버에서는 이를 기준으로 쿼리를 구성해야 한다.
2. 전체 개수나 페이지 수를 알기 어렵기 때문에, "총 n페이지 중 몇 페이지" 같은 정보를 제공하기 어렵다.
3. 정렬 기준이 복잡할 경우 (ex: createdAt, id 복합 정렬) 구현이 어려울 수 있다.