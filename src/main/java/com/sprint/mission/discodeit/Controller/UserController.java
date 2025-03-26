package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestPart("user") UserCreateRequest userCreateRequest,
                                              @RequestPart(name = "profile", required = false) MultipartFile profileFile) {
        BinaryContentCreateRequest binaryContentCreateRequest = null;
        if (profileFile != null && !profileFile.isEmpty()) {
            try {
                binaryContentCreateRequest = BinaryContentCreateRequest.builder()
                        .fileName(profileFile.getOriginalFilename())
                        .contentType(profileFile.getContentType())
                        .bytes(profileFile.getBytes())
                        .build();
            } catch (IOException e) {
                // 파일 처리 중 문제가 발생하면 적절한 예외 처리를 합니다.
                throw new RuntimeException("Error processing profile file", e);
            }
        }

        User createdUser = userService.create(userCreateRequest, Optional.ofNullable(binaryContentCreateRequest));
        UserDto createdUserDto = userService.find(createdUser.getId());
        return ResponseEntity.ok(createdUserDto);

    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto>updateUser(
            @PathVariable("userId") UUID userId,
            @RequestPart("user") UserUpdateRequest userUpdateRequest,
            @RequestPart(name="profile", required = false) MultipartFile profileFile){

        BinaryContentCreateRequest binaryContentCreateRequest = null;
        if (profileFile != null && !profileFile.isEmpty()) {
            try{
                binaryContentCreateRequest = BinaryContentCreateRequest.builder()
                        .fileName(profileFile.getOriginalFilename())
                        .contentType(profileFile.getContentType())
                        .bytes(profileFile.getBytes())
                        .build();
            }catch (IOException e){
                throw new RuntimeException("Error processing profile file", e);
            }
        }
        User updatedUser = userService.update(userId, userUpdateRequest, Optional.ofNullable(binaryContentCreateRequest));
        UserDto updatedUserDto = userService.find(updatedUser.getId());
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("{/userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}
