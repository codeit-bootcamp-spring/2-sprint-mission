package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Void>> save(@RequestBody SaveUserParamDto saveUserParamDto) {
        userService.save(saveUserParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> update(@RequestBody UpdateUserParamDto updateUserParamDto) {
        userService.update(updateUserParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.delete(deleteUserRequestDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/findAll")
    public ResponseEntity<ApiResponse<List<FindUserDto>>> findAll() {
        List<FindUserDto> findUserDtoList = userService.findAllUser();
        return ResponseEntity.ok(ApiResponse.success(findUserDtoList));
    }

    @PutMapping("/update-user-status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(@RequestBody UpdateUserStatusByUserIdParamDto updateUserStatusByUserIdParamDto) {
        userStatusService.updateByUserId(updateUserStatusByUserIdParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

}
