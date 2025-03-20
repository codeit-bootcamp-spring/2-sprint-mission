package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserFindDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    User createUser(UserCreateDto dto); //유저 생성

    UserFindDto getUserById(UUID userId); //유저 조회(UUID)

    String getUserNameById(UUID userId); //유저 이름 조회

    List<UserFindDto> findUsersByIds(Set<UUID> userIds);

    BinaryContent findProfileById(UUID userId);

    List<UserFindDto> getAllUsers();   //모든 유저 조회

    void updateUser(UserUpdateDto dto);

    void addChannel(UUID userID, UUID channelId);  //유저 채널 추가 (UUID 기반)

    void deleteUser(UUID userId);   //유저 삭제 (UUID 기반)

    void removeChannel(UUID userId, UUID channelId);   //유저 채널 삭제 (UUID 기반)

    void validateUserExists(UUID userId);   //유저 존재 확인(UUID)
}
