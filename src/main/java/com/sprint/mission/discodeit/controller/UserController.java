package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    // BasicUserService가 주입됨
    private final UserService userService;
    private final UserStatusService userStatusService;

    // ResponseEntity<> = API 응답의 세부사항을 명확하게 제어할 수 있음
    // ex) POST요청에 201반환, 에러처리

    // POST 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        UserResponseDTO createUser = userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    // PUT 사용자 정보 수정
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        userService.update(updateUserDTO);
        return ResponseEntity.ok().build();
    }

    // DELETE 사용자 삭제
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    // GET 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUser() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // PUT 사용자 온라인 상태 업데이트
    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(@PathVariable("userId") String userId,
                                 @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {
        userStatusService.update(updateUserStatusDTO);
        return ResponseEntity.ok().build();
    }
}
