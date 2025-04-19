package com.sprint.mission.discodeit.adapter.inbound.user;


import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserStatusRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserStatusResponse;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> register(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) {

    //파일이 존재하는지 안하는지 확인
    //파일이 존재하면 Optional로 감싸서 반환
    //널값 허용
    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    //Request DTO를 Command DTO로 변환
    //why? 컨트롤러단에서 서비스단으로 변수를 넘길 때, 관리하기 편하게 묶어서 보내려고
    // 또 영향을 컨트롤러단에서 서비스단에 영향을 적게 주려고
    CreateUserCommand command = UserDtoMapper.toCreateUserCommand(requestBody);

    //서비스단에서 컨트롤러 단으로 결과물을 DTO로 반환
    //Why? 서비스단에서 컨트롤러 단 영향을 적게 주기위해서
    UserResult result = userService.create(command, binaryContentRequest);

    return ResponseEntity.ok(UserDtoMapper.toCreateResponse(result));
  }

  //파일이 존재하면 DTO로 만든 뒤, Optional로 감싸서 반환
  private Optional<CreateBinaryContentCommand> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      //파일이 비어있으면 빈 값을 반환
      return Optional.empty();
    } else {
      try {
        //파일을 DTO로 변환시킴
        CreateBinaryContentCommand binaryContentCreateRequest = CreateBinaryContentCommand.create(
            profileFile);
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //전체 유저 목록을 반환시킴
  @GetMapping
  public ResponseEntity<List<UserResult>> findAll() {
    List<UserResult> result = userService.findAll();

    return ResponseEntity.ok(result);
  }

  //유저를 업데이트함
  //유저아이디를 PathVariable로 받고, RequestBody로 업데이트할 내용을 받음
  //추가로 이미지파일 역시 받음
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file) {

    //유저 생성할 때와 동일하게 DTO로 감싸는 작업
    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);
    UpdateUserCommand command = UserDtoMapper.toUpdateUserCommand(userId, requestBody);

    //서비스단에서 컨트롤러 단으로 결과물을 DTO로 반환
    //Why? 서비스단에서 컨트롤러 단 영향을 적게 주기위해서
    UserResult result = userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(UserDtoMapper.toCreateResponse(result));
  }

  //유저 삭제
  //삭제후 단순히 true, false 값만 반환
  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.ok(new UserDeleteResponse(true));
  }

  //온라인 상태 변환
  //유저 아이디와 현재 시각을 담은 request를 받음
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> online(@PathVariable UUID userId,
      @RequestBody UserStatusRequest requestBody) {

    //DTO로 감싸는 작업
    OnlineUserStatusCommand command = OnlineUserStatusCommand.create(userId,
        requestBody);

    UserStatusResult result = userService.online(command);

    return ResponseEntity.ok(UserStatusDtoMapper.toCreateResponse(result));
  }
}
