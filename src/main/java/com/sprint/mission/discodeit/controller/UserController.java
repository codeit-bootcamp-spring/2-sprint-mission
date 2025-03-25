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
    public ResponseEntity<?> save(@RequestBody SaveUserParamDto saveUserParamDto) {
        userService.save(saveUserParamDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateUserParamDto updateUserParamDto) {
        userService.update(updateUserParamDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.delete(deleteUserRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<FindUserDto>> findAll() {
        List<FindUserDto> findUserDtoList = userService.findAllUser();
        return ResponseEntity.ok().body(findUserDtoList);
    }

    @PostMapping("/updateUserStatus")
    public ResponseEntity<?> updateUserStatus(@RequestBody UpdateUserStatusByUserIdParamDto updateUserStatusByUserIdParamDto) {
        userStatusService.updateByUserId(updateUserStatusByUserIdParamDto);
        return ResponseEntity.ok().build();
    }

}
