package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.DTO.Request.UserRegisterRequestDTO;
import com.sprint.mission.discodeit.DTO.Request.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserLoginDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserUpdateDTO;
import com.sprint.mission.discodeit.DTO.legacy.User.UserFindDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserStatusService userStatusService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        UserCreateDTO userCreateDTO = userRegisterRequestDTO.userCreateDTO();
        Optional<BinaryContentCreateDTO> binaryContentCreateDTO = Optional.ofNullable(userRegisterRequestDTO.binaryContentCreateDTO());
        User user = userService.create(userCreateDTO, binaryContentCreateDTO);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        boolean isSuccess = authService.loginUser(userLoginDTO);
        if (isSuccess == true) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid id or password");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserFindDTO>> findAll() {
        List<UserFindDTO> all = userService.findAll();
        return ResponseEntity.ok(all);
    }

    @PutMapping("/online/{userId}")
    public ResponseEntity<UserStatus> online(@PathVariable String userId) {
        UserStatus userStatus = userStatusService.find(userId);
        userStatus.updateStatus();
        return ResponseEntity.ok(userStatus);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        UserUpdateDTO userUpdateDTO = userUpdateRequestDTO.userUpdateDTO();
        Optional<BinaryContentCreateDTO> binaryContentCreateDTO = Optional.ofNullable(userUpdateRequestDTO.binaryContentCreateDTO());
        User update = userService.update(userId, userUpdateDTO, binaryContentCreateDTO);

        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {
        boolean isDelete = userService.delete(userId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
