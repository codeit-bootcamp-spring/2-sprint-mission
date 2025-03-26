package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<UserDto> register(@RequestPart("user") UserCreateRequest userCreateRequest, @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest = Optional.ofNullable(profileFile)
                .map(file -> {
                    try {
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        User user = userService.create(userCreateRequest, optionalProfileCreateRequest);
        UserDto userDto = userService.find(user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "multipart/form-data")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestPart("user") UserUpdateRequest userUpdateRequest, @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest = Optional.ofNullable(profileFile)
                .map(file -> {
                    try {
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        User user = userService.update(id, userUpdateRequest, optionalProfileCreateRequest);
        UserDto userDto = userService.find(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findById(@PathVariable UUID id) {
        UserDto userDto  = userService.find(id);

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @RequestMapping(value = "/getAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }
}