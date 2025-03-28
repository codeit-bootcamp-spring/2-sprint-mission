package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        User user = userService.create(userCreateDto);
        return ResponseEntity.ok(BaseResponseDto.success(user.getId() + " 유저 등록이 완료되었습니다."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponseDto> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.update(userUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(user.getId() + " 유저 변경이 완료되었습니다."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponseDto> deleteUser(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok(BaseResponseDto.success(userId + " 유저 삭제가 완료되었습니다."));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponseDto> updateUserStatus(@PathVariable("id") UUID userId) {
        UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto = new UserStatusUpdateByUserIdDto(userId,
                Instant.now());
        userStatusService.updateByUserId(userStatusUpdateByUserIdDto);
        return ResponseEntity.ok(BaseResponseDto.success(userId + " 유저의 상태가 업데이트 되었습니다."));
    }
}
