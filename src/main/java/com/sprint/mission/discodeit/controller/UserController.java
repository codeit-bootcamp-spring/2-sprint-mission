package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateWithFileRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateWithFileRequest;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> create(
        @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        
        UserCreateWithFileRequest request = UserCreateWithFileRequest.of(userCreateRequest, profileImage);
        UserDto userDto = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> update(@PathVariable("userId") UUID userId,
        @RequestPart(value = "userUpdateRequest", required = false) UserUpdateRequest userUpdateRequest,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @RequestPart(value = "removeProfileImage", required = false) Boolean removeProfileImage) {

        UserUpdateWithFileRequest request = UserUpdateWithFileRequest.of(userUpdateRequest, profileImage, removeProfileImage);
        UserDto updatedUser = userService.update(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") String userIdStr) {
        UUID userId;
        userId = UUID.fromString(userIdStr);
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> findById(@PathVariable("userId") String userIdStr) {
        UUID userId;
        userId = UUID.fromString(userIdStr);
        UserDto user = userService.find(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}

