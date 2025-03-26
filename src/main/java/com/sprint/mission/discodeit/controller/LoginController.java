package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRep;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthServiceLoginRep> postLogin(
            @RequestPart("loginInfo") AuthServiceLoginReq authServiceLoginReq
    ) {
        AuthServiceLoginRep loginResponse = authService.login(authServiceLoginReq);
        return ResponseEntity.ok(loginResponse);
    }
}
