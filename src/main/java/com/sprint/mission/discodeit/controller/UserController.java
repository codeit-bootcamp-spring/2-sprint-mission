package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.UserApi;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @PostMapping(
        path = "",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Override
    public ResponseEntity<UserDto> create(
        @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserDto userDto = userService.save(userCreateRequest, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PatchMapping(
        path = "/{userId}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Override
    @PreAuthorize("hasRole('ADMIN') or principal.userId == #userId")
    public ResponseEntity<UserDto> update(
        @PathVariable("userId") UUID userId,
        @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        UserDto userDto = userService.update(userId, userUpdateRequest, profile);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }


    @DeleteMapping("/{userId}")
    @Override
    @PreAuthorize("hasRole('ADMIN') or principal.userId == #userId")
    public ResponseEntity<Void> delete(
        @PathVariable("userId") UUID userId
    ) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }
}
