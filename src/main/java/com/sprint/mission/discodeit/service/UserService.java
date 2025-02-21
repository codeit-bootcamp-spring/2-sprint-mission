package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    void createUser(String username); //유저 생성

    User getUser(String username);  //유저 조회
    List<User> getAllUsers();   //모든 유저 조회

    void updateUsername(User user, String newUsername);   //유저 이름 변경
    void addChannel(User user, String channelName);  //유저 채널 추가

    void deleteUser(User user);   //유저 삭제
    void deleteChannel(User user, String channelName);   //유저 채널 삭제
}
