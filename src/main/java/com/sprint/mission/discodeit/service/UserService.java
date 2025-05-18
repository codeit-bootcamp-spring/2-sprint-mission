package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
  UserDto createUser(CreateUserRequest request, MultipartFile profile); //유저 생성

  UserDto findUserById(UUID userId); //유저 조회(UUID)

  String findUserNameById(UUID userId); //유저 이름 조회

  List<UserDto> findUsersByIds(Set<UUID> userIds);

  BinaryContent findProfileById(UUID userId);

  List<UserDto> getAllUsers();   //모든 유저 조회

  UserDto updateUser(UUID userId, UpdateUserRequest request, MultipartFile profile);

  void deleteUser(UUID userId);   //유저 삭제 (UUID 기반)

  void validateUserExists(UUID userId);   //유저 존재 확인(UUID)

}
