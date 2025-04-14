package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

  // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
  User createUser(CreateUserRequest request); //유저 생성

  User findUserById(UUID userId); //유저 조회(UUID)

  String findUserNameById(UUID userId); //유저 이름 조회

  List<UserDto> findUsersByIds(Set<UUID> userIds);

  BinaryContent findProfileById(UUID userId);

  List<UserDto> getAllUsers();   //모든 유저 조회

  User updateProfile(UUID userId, BinaryContent binaryContent);

  User updateUser(UUID userId, UpdateUserRequest request);

  void deleteUser(UUID userId);   //유저 삭제 (UUID 기반)

  void validateUserExists(UUID userId);   //유저 존재 확인(UUID)

  UserDto mapToDto(User user);
}
