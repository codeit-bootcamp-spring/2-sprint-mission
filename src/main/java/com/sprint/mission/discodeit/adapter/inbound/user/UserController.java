package com.sprint.mission.discodeit.adapter.inbound.user;


import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateResult;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserDisplayList;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginRequestDTO;
import com.sprint.mission.discodeit.auth.AuthService;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.usecase.crud.UserService;
import com.sprint.mission.discodeit.core.user.usecase.status.UserStatusService;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  private final UserStatusService userStatusService;
  private final AuthService authService;

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserCreateResult> register(
      @RequestPart("user") UserCreateRequestDTO userCreateRequestDTO,
      @RequestPart(value = "profileImage", required = false) MultipartFile file
  ) throws IOException {

    Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();

    if (file != null && !file.isEmpty()) {
      binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    }

    UUID id = userService.create(userCreateRequestDTO, binaryContentRequest);

    return ResponseEntity.ok(new UserCreateResult(id));
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserLoginRequestDTO userLoginDTO) {
    authService.loginUser(userLoginDTO);
    return ResponseEntity.ok("로그인 성공");
  }

  @GetMapping("/findAll")
  public ResponseEntity<UserDisplayList> findAll() {
    UserDisplayList displayList = new UserDisplayList(userService.listAllUsers());
    return ResponseEntity.ok(displayList);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<String> delete(@PathVariable UUID userId) {

    userService.delete(userId);
    return ResponseEntity.ok("Delete successful");
  }

  @PutMapping("/online/{userId}")
  public ResponseEntity<UUID> online(@PathVariable UUID userId) {
    UserStatus userStatus = userStatusService.findByUserId(userId);
    userStatus.updatedTime();

    return ResponseEntity.ok(userStatus.getUserStatusId());
  }

  @PutMapping("/offline/{userId}")
  public ResponseEntity<UUID> offline(@PathVariable UUID userId) {
    UserStatus userStatus = userStatusService.findByUserId(userId);
    userStatus.setOffline();

    return ResponseEntity.ok(userStatus.getUserStatusId());
  }

//  @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public ResponseEntity<UUID> update(@PathVariable UUID userId,
//      @RequestPart("user") UpdateUserRequestDTO updateUserRequestDTO,
//      @RequestPart(value = "profileImage", required = false) MultipartFile file)
//      throws IOException {
//
//    Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();
//
//    if (file != null && !file.isEmpty()) {
//      binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
//          file.getOriginalFilename(),
//          file.getContentType(),
//          file.getBytes()
//      ));
//    }
//    UUID update = userService.update(userId, updateUserRequestDTO, binaryContentRequest);
//    return ResponseEntity.ok(update);
//  }
}
