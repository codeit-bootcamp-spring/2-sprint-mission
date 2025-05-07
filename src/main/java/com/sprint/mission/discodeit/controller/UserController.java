package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto; // User DTO
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest; // BinaryContentCreateRequest (MultipartFile 포함 가정)
import com.sprint.mission.discodeit.dto.request.UserCreateRequest; // User 생성 요청 DTO
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest; // User 업데이트 요청 DTO
import com.sprint.mission.discodeit.service.UserService; // UserService 인터페이스
import com.sprint.mission.discodeit.mapper.PageResponseMapper; // PageResponseMapper
import com.sprint.mission.discodeit.util.LogMapUtil; // 로깅 유틸리티
import io.swagger.v3.oas.annotations.tags.Tag; // Swagger 어노테이션
import java.util.List; // List 사용
import java.util.Optional; // Optional 사용
import java.util.UUID; // UUID 사용
import lombok.RequiredArgsConstructor; // Lombok
import org.slf4j.Logger; // 로깅
import org.slf4j.LoggerFactory; // 로깅
import org.springframework.http.HttpStatus; // HTTP 상태 코드
import org.springframework.http.MediaType; // 미디어 타입
import org.springframework.http.ResponseEntity; // 응답 객체
import org.springframework.web.bind.annotation.*; // 각종 매핑 어노테이션
import org.springframework.web.multipart.MultipartFile; // 파일 업로드 처리

@RequiredArgsConstructor // final 필드 의존성 주입
@RestController // REST 컨트롤러
@RequestMapping("/api/users") // 기본 요청 경로 유지
@Tag(name = "User", description = "User Api") // Swagger 태그 유지
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService; // UserService 주입
  private final PageResponseMapper pageResponseMapper; // PageResponseMapper 주입 (findAllByChannelId용)


  /**
   * 새로운 사용자를 생성하는 API 엔드포인트. POST /api/users 원본 코드 구조 및 명명 규칙 유지.
   *
   * @param userRequest 사용자 생성 요청 DTO (JSON).
   * @param profile     프로필 사진 파일 (선택 사항).
   * @return 생성된 User DTO와 HTTP 201 Created 또는 오류 상태.
   */
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // 소비 타입 유지
  public ResponseEntity<UserDto> create( // 메소드 이름 및 반환 타입 유지 (User -> UserDto)
      @RequestPart("userCreateRequest") UserCreateRequest userRequest, // 파트 이름 및 타입 유지
      @RequestPart(value = "profile", required = false) MultipartFile profile) { // 파트 이름 및 타입 유지

    // MultipartFile을 서비스 시그니처에 맞는 BinaryContentCreateRequest로 변환
    // resolveProfileRequest 헬퍼 메소드 사용 (내부 구현 수정)
    BinaryContentCreateRequest profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest) // resolveProfileRequest 호출
        .orElse(null);

    try {
      // 서비스에게 사용자 생성 로직 위임
      // 서비스 create 메소드는 UserDto를 반환합니다.
      UserDto userDto = userService.create(userRequest, profileRequest); // 서비스 호출

      log.info("{}", LogMapUtil.of("action", "createUser")
          .add("userDto", userDto)); // 로깅 유지

      // 생성 성공 시 201 Created 응답 반환
      return ResponseEntity.status(HttpStatus.CREATED).body(userDto); // 상태 코드 및 UserDto 반환 유지

    } catch (IllegalStateException e) {
      // 서비스에서 "동일한 username" 또는 "동일한 email" 예외 발생 시 409 Conflict 처리
      if (e.getMessage() != null && e.getMessage().contains("동일한")) {
        log.warn("Duplicate user creation attempt: {}", e.getMessage()); // 로깅 추가
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
      }
      // 그 외 예상치 못한 IllegalStateException
      log.error("Unexpected IllegalStateException during user creation", e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    } catch (IllegalArgumentException e) {
      // 서비스 create 메소드에서 IllegalArgumentException을 던지는 경우는 없지만, 예방적 처리
      log.error("Unexpected IllegalArgumentException during user creation", e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      log.error("Unexpected error during user creation", e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }

  /**
   * 특정 ID의 사용자 정보를 업데이트하는 API 엔드포인트. PUT /api/users/{userId} 원본 코드 구조 및 명명 규칙 유지.
   *
   * @param userId      업데이트할 사용자의 ID (UUID).
   * @param userRequest 사용자 업데이트 요청 DTO (JSON).
   * @param profile     새로운 프로필 사진 파일 (선택 사항).
   * @return 업데이트된 User DTO 또는 오류 상태.
   */
  @PutMapping(path = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // 매핑 및 소비 타입 유지
  public ResponseEntity<UserDto> update( // 메소드 이름 유지, 반환 타입 User -> UserDto
      @PathVariable UUID userId, // 경로 변수 유지
      @RequestPart("userUpdateRequest") UserUpdateRequest userRequest, // 파트 이름 및 타입 유지
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) { // 파트 이름 및 타입 유지

    // MultipartFile을 서비스 시그니처에 맞는 BinaryContentCreateRequest로 변환
    // resolveProfileRequest 헬퍼 메소드 사용 (내부 구현 수정)
    BinaryContentCreateRequest profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest) // resolveProfileRequest 호출
        .orElse(null);

    try {
      // 서비스에게 사용자 업데이트 로직 위임
      // 서비스 update 메소드는 UserDto를 반환합니다.
      UserDto updatedUserDto = userService.update(userId, userRequest, profileRequest); // 서비스 호출

      log.info("{}", LogMapUtil.of("action", "updateUser")
          .add("updatedUser", updatedUserDto)); // 로깅 유지 (updatedUser 값은 DTO로 변경)

      // 업데이트 성공 시 200 OK와 함께 업데이트된 User DTO 반환
      return ResponseEntity.ok(updatedUserDto); // 상태 코드 및 UserDto 반환 유지

    } catch (IllegalStateException e) {
      // 서비스 update 메소드에서 사용자를 찾을 수 없을 경우 예외 발생 (user not found)
      log.warn("User not found for update with id: {}", userId, e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
    } catch (IllegalArgumentException e) {
      // 서비스 update 메소드에서 중복 username/email 예외 발생 시 처리
      log.warn("Update failed due to duplicate data for user {}: {}", userId,
          e.getMessage()); // 로깅 추가
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      log.error("Unexpected error updating user with id: {}", userId, e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }

  /**
   * 모든 사용자 목록을 조회하는 API 엔드포인트. GET /api/users 원본 코드 구조 및 명명 규칙 유지. 페이징은 적용되지 않음 (서비스에 해당 메소드가
   * List<UserDto> 반환).
   *
   * @return 모든 User DTO 목록 또는 404 Not Found (사용자 없을 경우 서비스에서 예외 발생).
   */
  @GetMapping // 매핑 유지 (/api/users)
  public ResponseEntity<List<UserDto>> readAll() { // 메소드 이름 및 반환 타입 유지

    try {
      // 서비스에게 모든 사용자 조회 로직 위임
      // 서비스 readAll 메소드는 List<UserDto>를 반환합니다.
      List<UserDto> userDtos = userService.readAll(); // 서비스 호출

      log.info("{}", LogMapUtil.of("action", "readAllUsers")
          .add("userDtos", userDtos)); // 로깅 유지

      // 조회 성공 시 200 OK와 함께 User DTO 목록 반환
      return ResponseEntity.ok(userDtos); // 상태 코드 및 List<UserDto> 반환 유지

    } catch (IllegalArgumentException e) {
      // 서비스 readAll 메소드에서 사용자가 없을 경우 발생시키는 예외 처리
      log.warn("No users found: {}", e.getMessage()); // 로깅 추가
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(null); // 404 Not Found (원본 서비스에 따라 예외 발생 시 404)
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      log.error("Unexpected error getting all users", e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }

  /**
   * 특정 ID의 단일 사용자를 조회하는 API 엔드포인트. GET /api/users/{userId} 원본 코드에는 없었지만, 서비스에 존재하는 read 메소드를
   * 노출합니다.
   *
   * @param userId 조회할 사용자의 ID (UUID).
   * @return 조회된 User DTO 또는 404 Not Found 상태.
   */
  @GetMapping("/{userId}") // {userId} 경로 변수 매핑
  public ResponseEntity<UserDto> read(@PathVariable UUID userId) { // 메소드 이름 read로 추가

    try {
      // 서비스에게 사용자 ID로 조회 로직 위임
      // 서비스 read 메소드는 UserDto를 반환합니다.
      UserDto userDto = userService.read(userId); // 서비스 호출

      // 로깅은 필요시 추가

      // 조회 성공 시 200 OK와 함께 User DTO 반환
      return ResponseEntity.ok(userDto);

    } catch (IllegalArgumentException e) {
      // 서비스 read 메소드에서 사용자를 찾을 수 없을 경우 발생시키는 예외 처리
      log.warn("User not found for id: {}", userId); // 로깅 추가
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      log.error("Unexpected error getting user by id: {}", userId, e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }


  /**
   * 특정 ID의 사용자를 삭제하는 API 엔드포인트. DELETE /api/users/{userId} 원본 코드 구조 및 명명 규칙 유지.
   *
   * @param userId 삭제할 사용자의 ID (UUID).
   * @return 삭제 성공 시 204 No Content 또는 404 Not Found 상태.
   */
  @DeleteMapping("/{userId}") // 매핑 유지
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) { // 메소드 이름 및 경로 변수 유지
    try {
      // 서비스에게 사용자 삭제 로직 위임
      // 서비스 delete 메소드는 void를 반환합니다.
      userService.delete(userId); // 서비스 호출

      log.info("{}", LogMapUtil.of("action", "deleteUser")
          .add("userKey", userId)); // 로깅 유지

      // 삭제 성공 시 204 No Content 반환
      return ResponseEntity.noContent().build(); // 상태 코드 유지

    } catch (IllegalStateException e) {
      // 서비스 delete 메소드에서 사용자를 찾을 수 없을 경우 예외 발생
      log.warn("User not found for deletion with id: {}", userId); // 로깅 추가
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      log.error("Unexpected error deleting user with id: {}", userId, e); // 로깅 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }

  /**
   * 특정 사용자의 상태 정보를 업데이트하는 API 엔드포인트.
   * PATCH /api/users/{userId}/userStatus
   * 원본 코드 그대로 유지 (UserService가 아닌 UserStatusService 호출).
   *
   * @param userId 업데이트할 사용자의 ID (UUID).
   * @param request 상태 업데이트 요청 DTO.
   * @return 업데이트된 UserStatus 엔티티 또는 오류 상태.
   */


  /**
   * MultipartFile 객체로부터 BinaryContentCreateRequest DTO를 생성하는 헬퍼 메소드. 원본 코드의 resolveProfileRequest
   * 메소드 구현을 수정하여 MultipartFile 객체 자체를 DTO에 담도록 합니다.
   *
   * @param profileFile MultipartFile 객체 (null 또는 비어있을 수 있음).
   * @return BinaryContentCreateRequest DTO를 담은 Optional 또는 파일이 없으면 Optional.empty().
   */
  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) { // null 체크 추가 및 isEmpty() 사용
      return Optional.empty();
    } else {
      // 기존 getBytes() 호출 및 IOException 처리 로직 제거
      // BinaryContentCreateRequest DTO에 MultipartFile 필드가 있다고 가정
      BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(), // 파일명 획득
          profileFile.getContentType(), // 컨텐츠 타입 획득
          profileFile // <<< MultipartFile 객체 자체를 DTO에 담아 전달
      );
      return Optional.of(binaryContentCreateRequest);
    }
    // 기존 IOException 예외 처리는 이제 필요 없음 (getBytes() 사용 안 함)
  }
}
