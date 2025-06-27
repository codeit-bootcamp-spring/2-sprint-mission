package com.sprint.mission.discodeit.core.user.controller;


import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.service.UserService;
import com.sprint.mission.discodeit.core.user.dto.response.UserDto;
import com.sprint.mission.discodeit.swagger.UserApi;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class UserController implements UserApi {

  private final UserService userService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) throws IOException {

    Optional<BinaryContentCreateRequest> optional = Optional.empty();

    if (!file.isEmpty()) {
      BinaryContentCreateRequest binaryRequest = BinaryContentCreateRequest.create(file);
      optional = Optional.of(binaryRequest);
    }

    UserDto userDto = userService.create(request, optional);

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> result = userService.findAll();

    return ResponseEntity.ok(result);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") @Valid UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile file) throws IOException {
    Optional<BinaryContentCreateRequest> optional = Optional.empty();

    if (!file.isEmpty()) {
      BinaryContentCreateRequest binaryRequest = BinaryContentCreateRequest.create(file);
      optional = Optional.of(binaryRequest);
    }

    UserDto userDto = userService.update(userId, request, optional);
    return ResponseEntity.ok(userDto);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusRequest request) {
    UserStatusDto result = userService.online(userId, request);
    return ResponseEntity.ok(result);
  }
}
