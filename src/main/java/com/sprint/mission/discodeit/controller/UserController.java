package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserCreateResponseDto> create(@RequestBody UserCreateRequestDto dto) {
        UserCreateResponseDto response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
