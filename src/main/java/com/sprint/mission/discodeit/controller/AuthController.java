package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.User.LoginRequest;
import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final BasicAuthService authService;
    private final UserStatusRepository userStatusRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public UserStatus login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User status not found for user ID: " + user.getId()));

        userStatus.updateLastActivatedAt(Instant.now());
        userStatusRepository.save(userStatus);

        return userStatus;
    }
}
