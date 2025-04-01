package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.User.UserCreateRequest;
import com.sprint.mission.discodeit.dto.User.UserDto;
import com.sprint.mission.discodeit.dto.User.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFound;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    //사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request, Optional.empty());
    }

    //사용자 정보 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        return userService.update(id, request, Optional.empty());
    }

    //사용자 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    //모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAllUsers() {
        return userService.findAll();
    }

    //사용자 온라인 상태 업데이트
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/status")
    public UserStatus updateOnlineStatus(@PathVariable UUID id, @RequestBody UpdateUserStatusDto statusDto){
        if (!userService.exists(id)) {
            throw new UserNotFound("User not found: " + id);
        }
        return userStatusService.update(id, statusDto);
    }

    //사용자 목록 조회
    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
