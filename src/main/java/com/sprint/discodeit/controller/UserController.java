package com.sprint.discodeit.controller;

import com.sprint.discodeit.domain.StatusType;
import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.domain.dto.userDto.UserResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserUpdateRequestDto;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.service.UserService;
import com.sprint.discodeit.service.UserServiceV1;
import com.sprint.discodeit.service.basic.users.BasicUserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceV1 basicUserService;
    private UserResponseDto userResponseDto;

    @PostMapping("/create")
    public ResponseEntity<UserNameStatusResponseDto> signup(@RequestBody UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto){
        UserNameStatusResponseDto userNameStatusResponseDto = basicUserService.create(userRequestDto,
                userProfileImgResponseDto);
        return ResponseEntity.ok(userNameStatusResponseDto);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponseDto> update(@PathVariable String userId, @RequestBody UserUpdateRequestDto updateRequestDto){
        UserResponseDto userResponseDto = basicUserService.update(updateRequestDto, userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> get(@PathVariable String userId){
        userResponseDto = basicUserService.find(UUID.fromString(userId));
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll(){
        List<User> all = basicUserService.findAll();
        return ResponseEntity.ok(all);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<String> delet(@PathVariable String userId){
        basicUserService.delete(UUID.fromString(userId));
        return ResponseEntity.ok(StatusType.Inactive.getExplanation());
    }

}
