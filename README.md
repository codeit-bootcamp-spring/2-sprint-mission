# Sprint9 - 인증, 인가

## Spring Security 환경 설정

- [x] 프로젝트에 Spring Security 의존성을 추가하세요.
- [x] Security 설정 클래스를 생성하세요.
  ~~~text
  - 패키지명: com.sprint.mission.discodeit.config
  - 클래스명: SecurityConfig
  ~~~
- [x] SecurityFilterChain Bean을 선언하세요.
- [x] 가장 기본적인 SecurityFilterChain을 등록하고, 이때 등록되는 필터 목록을 디버깅해보세요. 필터 목록은 PR에 첨부하세요.
- [x] 모든 요청에 대해 인증이 수행되도록 하세요.
- [x] /api/를 포함하지 않는 모든 url에 대한 요청(정적 리소스, swagger, actuator 등)은 인증을 수행하지 않도록 하세요.
- [x] LogoutFilter를 제외하세요.
  ~~~text
  - 디스코드잇은 로그아웃 페이지를 CSR로 처리하기 때문에 LogoutFilter는 사용하지 않습니다.
  ~~~
- [x] 개발 환경에서 Spring Security 모듈의 로깅 레벨을 trace로 설정하세요.
  ~~~text
  - 각 요청마다 통과하는 필터 목록을 확인할 수 있습니다.
  ~~~

## CSRF 보호 설정하기

- [x] CSRF 토큰을 발급하는 API를 구현하세요
- [x] CSRF 토큰을 발급하는 API는 인증하지 않도록 SecurityFilterChain을 리팩토링하세요.

## 회원가입 고도화

- [x]  회원가입 API 스펙은 유지합니다.
- [x]  회원가입 시 비밀번호는 PasswordEncoder를 통해 해시로 저장하세요.
    ~~~text
    - PasswordEncoder의 구현체는 BCryptPasswordEncoder를 활용하세요.
    ~~~~
- [x] 회원가입 API는 인증하지 않도록 SecurityFilterChain을 리팩토링하세요.

## 기본 인증 구현

- 다음의 조건을 만족하는 필터와 AuthenticationProvider를 구현하세요.
- [ ]  로그인 API 스펙은 다음과 같습니다.
  - 기존에 구현했던 로그인 관련 코드는 제거하세요.
    ~~~text
        - AuthApi.login, AuthController.login
        - AuthService.login
        - LoginRequest
    ~~~~
- [ ]  다음의 주요 컴포넌트를 활용해 Spring Security의 기본 인증 플로우를 최대한 유지합니다.
    ~~~text
    - 인증 플로우 참고: UsernamePasswordAuthenticationFilter
    - AuthenticationProvider: DaoAuthenticationProvider
    - SecurityContextRespository: HttpSessionSecurityContextRepository
    ~~~~

## 세션을 활용한 현재 사용자 정보 조회

- [ ] 세션ID를 통해 사용자의 기본 정보(UserDto)를 가져올 수 있도록 API를 정의하세요.

## 로그아웃

- 다음의 조건을 만족하는 필터를 구현하세요.
- [ ] 로그아웃 API 스펙은 다음과 같습니다.
- [ ] CSRF 검사는 수행하지 않도록 합니다.
- [ ] 해당 세션을 무효화 처리하세요.
- [ ] SecurityContext를 초기화하세요.

## 권한 관리

- [ ]  다음과 같이 권한을 정의하세요.
    ~~~ text 
    관리자: ROLE_ADMIN
    채널 매니저: ROLE_CHANNEL_MANAGER
    일반 사용자: ROLE_USER
    각 권한은 계층 구조를 가집니다.
    관리자 > 채널 매니저 > 일반 사용자
    ~~~
- [ ]  회원 가입 시 모든 사용자는 ROLE_USER 권한을 가집니다.
- [ ]  애플리케이션 실행 시 ROLE_ADMIN 권한을 가진 계정이 초기화되도록 구현하세요.
- [ ]  UserDto에 권한 정보를 포함하세요.
- [ ]  사용자 권한을 수정하는 API를 구현하세요.

## 인가 처리

- [ ] 회원가입, 로그인, csrf 토큰 발급 등을 제외한 모든 API는 최소 ROLE_USER 권한을 가져야합니다.
- [ ] 퍼블릭 채널 생성, 수정, 삭제는 최소 ROLE_CHANNEL_MANAGER 권한을 가져야합니다.
- [ ] 사용자 권한 수정은 ROLE_ADMIN 권한을 가져야합니다.

# 심화 요구사항

## Remember-Me

- 다음의 조건을 만족하도록 로그인 유지 기능을 구현하세요.
- [ ] 토큰은 데이터베이스에 저장하세요.
- [ ] 쿠키에 저장되는 토큰의 유효기간은 3주로 지정하세요.
- [ ] 로그아웃 시 데이터베이스에 저장된 토큰을 삭제하고, 클라이언트 쿠키도 삭제하세요.

## 동시 로그인 제한

- [ ] 하나의 사용자 ID로 동시 로그인을 제한하세요. 새로운 로그인 발생 시 기존 세션을 무효화하세요.

## 세션 고정 보호

- [ ] 세션 고정 보호를 위해 필요한 설정을 구현하세요.

## 사용자 로그인 상태 고도화

- [ ] Session 정보를 활용해 사용자의 로그인 상태를 판단하도록 리팩토링하세요.
- [ ] UserStatus 엔티티와 관련된 모든 코드는 삭제하세요.

## 인가 고도화

- [ ] 사용자 정보 수정, 삭제는 본인 또는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있습니다.
- [ ] 메시지 수정은 메시지를 작성한 사람만 호출할 수 있습니다.
- [ ] 메시지 삭제는 메시지를 작성한 사람 또는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있습니다.
- [ ] 읽음 상태 생성, 수정은 본인만 호출할 수 있습니다.