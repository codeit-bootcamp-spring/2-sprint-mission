package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestPart("user") @Valid CreateUserRequestDTO createUserRequestDTO,
                                                            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        CreateUserParam createUserParam = userMapper.toCreateUserParam(createUserRequestDTO);
        UserDTO userDTO = null;
        try {
            userDTO = userService.create(createUserParam, multipartFile); // try-catch 빼주고 전역예외핸들링으로 바꿀 예정
        } catch (IOException e) {
        }

        CreateUserResponseDTO createUserResponseDTO = userMapper.toCreateUserResponseDTO(userDTO);

        return ResponseEntity.ok(createUserResponseDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("userId") UUID id) {
        UserDTO userDTO = userService.find(id);
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userDTO);

        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/findAll")
    public ResponseEntity<UserListResponseDTO> getUserAll() {
        List<UserDTO> userDTOList = userService.findAll();
        UserListResponseDTO userListResponseDTO = new UserListResponseDTO(userDTOList);

        return ResponseEntity.ok(userListResponseDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable("userId") UUID id,
                                                            @RequestPart("user") @Valid UpdateUserRequestDTO updateUserRequestDTO,
                                                            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        UpdateUserParam updateUserParam = userMapper.toUpdateUserParam(updateUserRequestDTO);
        UpdateUserDTO updateUserDTO = userService.update(id, updateUserParam, multipartFile);

        return ResponseEntity.ok(userMapper.toUpdateUserResponseDTO(updateUserDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteUserResponseDTO> deleteUser(@PathVariable("userId") UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(new DeleteUserResponseDTO(id, id + "번 회원 삭제 완료"));
    }
}
