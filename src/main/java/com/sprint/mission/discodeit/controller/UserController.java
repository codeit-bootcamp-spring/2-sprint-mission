package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userList = userService.findAll();

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        UserDto user = userService.findByUserId(userId);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserDto> createUser(
        @RequestPart(value = "userCreateRequest") CreateUserRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile file)
        throws Exception {

        UserDto response = null;
        if (file == null || file.isEmpty()) {
            response = userService.create(request);
        } else {
            CreateBinaryContentRequest binaryContentRequest =
                convertFileToRequest(file);

            response = userService.create(request, binaryContentRequest);
        }

        return ResponseEntity.ok(response);
    }

    private CreateBinaryContentRequest convertFileToRequest(MultipartFile file)
        throws IOException {
        return new CreateBinaryContentRequest(file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes());
    }

    @PatchMapping("/api/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
        @RequestPart(value = "userUpdateRequest") UpdateUserRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile file)
        throws IOException {

        UserDto response = null;
        if (file == null || file.isEmpty()) {
            response = userService.update(userId, request);

        } else {
            CreateBinaryContentRequest binaryContentRequest =
                convertFileToRequest(file);

            response = userService.update(userId, request, binaryContentRequest);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.remove(userId);

        return ResponseEntity.noContent().build();
    }
}
