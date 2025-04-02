package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/create")
  public ResponseEntity<UserDto> createUser(
      @RequestParam("userCreateRequest") String userJson,
      @RequestParam("profile") Optional<MultipartFile> profileFile) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    UserCreateRequest userCreateRequest = objectMapper.readValue(userJson, UserCreateRequest.class);

    Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
      try {
        return new BinaryContentCreateRequest(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    User createdUser = userService.create(userCreateRequest, profileCreateRequest);
    UserDto userDto = userService.find(createdUser.getId());

    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}")
  private ResponseEntity<UserDto> updateUser(
      @PathVariable UUID userId,
      @RequestParam("userUpdateRequest") String userJson,
      @RequestParam("profile") Optional<MultipartFile> profileFile) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    UserUpdateRequest userUpdateRequest = objectMapper.readValue(userJson, UserUpdateRequest.class);

    Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
      try {
        return new BinaryContentCreateRequest(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );
      } catch (IOException e) {
        throw new RuntimeException("File processing error", e);
      }
    });

    User updatedUser = userService.update(userId, userUpdateRequest, profileCreateRequest);
    UserDto userDto = userService.find(updatedUser.getId());

    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  private ResponseEntity<Void> deleteUser(
      @PathVariable UUID userId) {

    userService.delete(userId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    List<UserDto> userDtos = userService.findAll();

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }
}


