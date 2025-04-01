package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestPart("userInfo") UserCreateDto userCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        Optional<BinaryContentCreateDto> contentCreate = Optional.empty();
        if (file != null && !file.isEmpty()) {
            try {
                contentCreate = Optional.of(new BinaryContentCreateDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        User user = userService.create(userCreateRequest, contentCreate);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestPart("newUserInfo") UserUpdateDto userUpdateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        Optional<BinaryContentCreateDto> contentCreate = Optional.empty();
        if (file != null && !file.isEmpty()) {
            try {
                contentCreate = Optional.of(new BinaryContentCreateDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        User user = userService.update(userUpdateRequest, contentCreate);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(
            @RequestBody UserDeleteDto userDeleteRequest
    ) {
        userService.delete(userDeleteRequest);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<UserFindResponseDto> findUser(
            @RequestBody UserFindRequestDto userFindRequest
    ) {
        UserFindResponseDto userFindResponse = userService.find(userFindRequest);
        return ResponseEntity.ok(userFindResponse);
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<UserFindAllResponseDto>> findAllUser() {
        List<UserFindAllResponseDto> userFindAllResponse = userService.findAllUser();
        return ResponseEntity.ok(userFindAllResponse);
    }

}
