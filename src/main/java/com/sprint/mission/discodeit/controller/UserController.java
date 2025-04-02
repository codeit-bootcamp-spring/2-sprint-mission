package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("")
  public ResponseEntity<ApiResponse<Void>> save(
      @ModelAttribute SaveUserRequestDto saveUserRequestDto,
      @RequestParam(value = "file", required = false) MultipartFile file
  ) throws IOException {
    userService.save(saveUserRequestDto, SaveBinaryContentRequestDto.nullableFrom(file));
    return ResponseEntity.ok(ApiResponse.success());
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> update(
      @PathVariable UUID userId,
      @ModelAttribute UpdateUserRequestDto updateUserRequestDto,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    userService.update(userId, updateUserRequestDto,
        SaveBinaryContentRequestDto.nullableFrom(file));
    return ResponseEntity.ok(ApiResponse.success());
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> delete(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @GetMapping("/findAll")
  public ResponseEntity<ApiResponse<List<FindUserDto>>> findAll() {
    List<FindUserDto> findUserDtoList = userService.findAllUser();
    return ResponseEntity.ok(ApiResponse.success(findUserDtoList));
  }
}
