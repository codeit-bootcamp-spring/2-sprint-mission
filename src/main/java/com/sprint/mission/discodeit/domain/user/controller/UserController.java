package com.sprint.mission.discodeit.domain.user.controller;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResult> register(
      @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {
    BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profile);
    UserResult user = userService.register(userCreateRequest, binaryContentRequest);

    return ResponseEntity.ok(user);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResult> getById(@PathVariable UUID userId) {
    UserResult user = userService.getById(userId);

    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<UserResult>> getAll() {
    List<UserResult> users = userService.getAllIn();

    return ResponseEntity.ok(users);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResult> updateUser(@PathVariable UUID userId,
      @Valid @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {
    BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profile);
    UserResult updatedUser = userService.update(userId, userUpdateRequest, binaryContentRequest);

    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);

    return ResponseEntity.noContent().build();
  }

  private BinaryContentRequest getBinaryContentRequest(MultipartFile profileImage) {
    if (profileImage == null) {
      return null;
    }

    return BinaryContentRequest.fromMultipartFile(profileImage);
  }

}
