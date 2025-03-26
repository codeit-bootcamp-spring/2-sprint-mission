package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.display.UserDisplayList;
import com.sprint.mission.discodeit.dto.create.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.dto.create.UserCreateRequestDTO;
import com.sprint.mission.discodeit.dto.result.LoginResultDTO;
import com.sprint.mission.discodeit.dto.update.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.dto.result.CreateUserResult;
import com.sprint.mission.discodeit.dto.update.UserLoginRequestDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateUserResult> register(
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

        return ResponseEntity.ok(new CreateUserResult(id));
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

    @DeleteMapping
    public ResponseEntity<String> delete(HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        userService.delete(userId);
        return ResponseEntity.ok("Delete successful");
    }

    @PutMapping("/online")
    public ResponseEntity<UUID> online(HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");

        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatus.updatedTime();

        return ResponseEntity.ok(userStatus.getUserStatusId());
    }

    @PutMapping("/offline")
    public ResponseEntity<UUID> offline(HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");

        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatus.setOffline();

        return ResponseEntity.ok(userStatus.getUserStatusId());
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> update(@RequestPart("user") UpdateUserRequestDTO updateUserRequestDTO,
                                       @RequestPart(value = "profileImage", required = false) MultipartFile file,
                                       HttpServletRequest httpRequest) throws IOException {

        UUID userId = (UUID) httpRequest.getAttribute("userId");

        Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();

        if (file != null && !file.isEmpty()) {
            binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            ));
        }

        UUID update = userService.update(userId, updateUserRequestDTO, binaryContentRequest);

        return ResponseEntity.ok(update);
    }
}
