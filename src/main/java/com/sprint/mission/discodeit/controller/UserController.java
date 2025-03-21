package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.display.UserDisplayList;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.request.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.result.CreateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateUserResult> register(
            @RequestPart("user") CreateUserRequestDTO createUserRequestDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile file

    ) throws IOException {

        Optional<CreateBinaryContentRequestDTO> binaryContentRequest = Optional.empty();

        if (file != null && !file.isEmpty()) {
            binaryContentRequest = Optional.of(new CreateBinaryContentRequestDTO(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            ));
        }
        UUID id = userService.create(createUserRequestDTO, binaryContentRequest);

        return ResponseEntity.ok(new CreateUserResult(id));
    }


    @GetMapping
    public ResponseEntity<UserDisplayList> findAll() {
        UserDisplayList displayList = new UserDisplayList(userService.listAllUsers());
        return ResponseEntity.ok(displayList);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<byte[]> getUserProfile(@PathVariable UUID id) {
        UserFindDTO user = userService.findById(id);

        if (user.profileId() == null) {
            return ResponseEntity.notFound().build();
        }

        BinaryContent profileImage = binaryContentService.findById(user.profileId());

        if (profileImage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(profileImage.getContentType()))
                .body(profileImage.getBytes());
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok("Delete successful");
    }

    //    @PutMapping("/online/{userId}")
//    public ResponseEntity<UserStatus> online(@PathVariable String userId, @RequestBody UserStatusDTO UserstatusDTO) {
//        UserStatus userStatus = userStatusService.findByUserId(userId);
//
//        return ResponseEntity.ok(userStatus);
//    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
//        UserUpdateDTO userUpdateDTO = userUpdateRequestDTO.userUpdateDTO();
//        Optional<BinaryContentCreateDTO> binaryContentCreateDTO = Optional.ofNullable(userUpdateRequestDTO.binaryContentCreateDTO());
//        User update = userService.update(userId, userUpdateDTO, binaryContentCreateDTO);
//
//        return ResponseEntity.ok(update);
//    }
}
