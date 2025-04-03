package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@RequestPart("userCreateRequest") UserCreateDto userCreateDto,
                                           @RequestPart(name = "profile", required = false) MultipartFile file) {
        Optional<BinaryContentCreateDto> profileRequest = Optional.ofNullable(file)
                .flatMap(this::resolveProfileRequest);
        User createdUser = userService.create(userCreateDto, profileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateDto userUpdateDto,
            @RequestPart(name = "profile", required = false) MultipartFile file
    ) {
        Optional<BinaryContentCreateDto> profileRequest = Optional.ofNullable(file)
                .flatMap(this::resolveProfileRequest);
        User updatedUser = userService.update(userId, userUpdateDto, profileRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("{userId}/userStatus")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
                                                               @RequestBody UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateByUserIdDto);
        return ResponseEntity.ok(userStatus);
    }

    private Optional<BinaryContentCreateDto> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
