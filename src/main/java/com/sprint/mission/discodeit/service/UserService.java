package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    void createUser(String username); //유저 생성

    User getUserByName(String username);  //유저 조회(Name)
    User getUserById(UUID userId); //유저 조회(UUID)
    UUID getUserIdByName(String userName); //유저 UUID 조회
    String getUserNameByid(UUID userId); //유저 이름 조회
    List<User> getAllUsers();   //모든 유저 조회

    void updateUsername(UUID userId, String newUsername);   //유저 이름 변경 (UUID 기반)
    void addChannel(UUID userID, UUID channelId);  //유저 채널 추가 (UUID 기반)

    void deleteUser(UUID userId);   //유저 삭제 (UUID 기반)
    void deleteChannel(UUID userId, UUID channelId);   //유저 채널 삭제 (UUID 기반)
    void validateUserExists(UUID userId);   //유저 존재 확인(UUID)
    void validateUserExists(String username); //유저 존재 확인(Name)
}
