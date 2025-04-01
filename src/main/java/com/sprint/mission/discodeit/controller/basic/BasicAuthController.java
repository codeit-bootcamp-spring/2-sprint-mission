package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BasicAuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginRequest request){
        UserDto loginResponse = authService.login(request);
        logger.info("Successfully login user :{}", loginResponse);
        return ResponseEntity.ok(loginResponse);
    }
}
