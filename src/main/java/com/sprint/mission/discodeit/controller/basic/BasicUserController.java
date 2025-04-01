package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.controller.BinaryContentRequestHandler;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class BasicUserController {

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final BinaryContentRequestHandler binaryContentRequestHandler;
    private static final Logger logger = LoggerFactory.getLogger(BasicUserController.class);

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> create(
            @RequestPart("user") UserCreateRequest request,
            @RequestPart(value = "binaryContent", required = false) MultipartFile binaryContent) {

        Optional<BinaryContentCreateRequest> optionalBinaryContent = binaryContentRequestHandler.handle(binaryContent);

        UserDto response = userService.create(request, optionalBinaryContent);
        logger.info("Successfully created user: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> update(
            @RequestPart("user") UserUpdateRequest request,
            @RequestPart(value = "binaryContent", required = false) MultipartFile binaryContent) {

        Optional<BinaryContentCreateRequest> optionalBinaryContent = binaryContentRequestHandler.handle(binaryContent);

        UserDto response = userService.update(request, optionalBinaryContent);
        logger.info("Successfully updated user: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<String> delete(@PathVariable String userId) {
            userService.delete(UUID.fromString(userId));
            logger.info("Successfully deleted user: {}", UUID.fromString(userId));
            return ResponseEntity.ok("Successfully deleted user: " + userId);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        logger.info("Successfully find all users: {}", users);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/updateStatus")
    public ResponseEntity<UserStatus> updateStatus(@PathVariable String userId, @RequestBody UserStatusUpdateRequest request) {
        UserStatus response = userStatusService.update(UUID.fromString(userId), request);
        logger.info("Successfully updated user status: {}", response);
        return ResponseEntity.ok(response);
    }
}
