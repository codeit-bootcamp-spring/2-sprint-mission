package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final FileBinaryContentRepository fileBinaryContentRepository;

    public UserController(UserService userService, BinaryContentService binaryContentService, FileBinaryContentRepository fileBinaryContentRepository) {
        this.userService = userService;
        this.binaryContentService = binaryContentService;
        this.fileBinaryContentRepository = fileBinaryContentRepository;
    }

    // 사용자 생성 (프로필 이미지 업로드 가능)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@RequestPart UserCreateRequest userCreateRequest,
                                           @RequestPart("file") MultipartFile file) {
        try{
            BinaryContent binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );

            BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                    binaryContent.getFileName(), binaryContent.getContentType(),
                    binaryContent.getBytes()
            );

            User user = userService.create(userCreateRequest, Optional.of(request));
            binaryContentService.create(request);
            return ResponseEntity.ok(user);
        }
        catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // 특정 사용자 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        UserDto userDto = userService.find(userId);
        return ResponseEntity.ok(userDto);
    }

    // 모든 사용자 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 사용자 정보 수정 (프로필 이미지 업데이트 가능)
    @PutMapping(value = "/{userId}" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable UUID userId,
                                           @RequestPart UserUpdateRequest userUpdateRequest,
                                           @RequestPart("file") MultipartFile file) {

        try{
            BinaryContent binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );

            BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                    binaryContent.getFileName(), binaryContent.getContentType(),
                    binaryContent.getBytes()
            );

            User updatedUser = userService.update(userId, userUpdateRequest, Optional.of(request));
            binaryContentService.create(request);
            return ResponseEntity.ok(updatedUser);
        }
        catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId, @RequestPart UUID fileId) {
        userService.delete(userId);
        binaryContentService.delete(fileId);
        return ResponseEntity.ok("사용자가 삭제되었습니다.");
    }
}

