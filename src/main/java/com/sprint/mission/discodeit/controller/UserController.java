package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createUser(
            @RequestParam("user") String userJson,
            @RequestParam("profile") Optional<MultipartFile> profileFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserCreateRequest userCreateRequest = objectMapper.readValue(userJson, UserCreateRequest.class);

        Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
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

        User createdUser = userService.create(userCreateRequest, profileCreateRequest);
        UserDto userDto = userService.find(createdUser.getId());

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.PUT)
    private ResponseEntity<UserDto> updateUser(
            @PathVariable UUID userId,
            @RequestParam("userUpdate") String userJson,
            @RequestParam("profile") Optional<MultipartFile> profileFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserUpdateRequest userUpdateRequest = objectMapper.readValue(userJson, UserUpdateRequest.class);

        Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
            try {
                return new BinaryContentCreateRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException("File processing error", e);
            }
        });

        User updatedUser = userService.update(userId, userUpdateRequest, profileCreateRequest);
        UserDto userDto = userService.find(updatedUser.getId());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    private ResponseEntity<Void> deleteUser(
            @PathVariable UUID userId) {

        userService.delete(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtos = userService.findAll();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}


