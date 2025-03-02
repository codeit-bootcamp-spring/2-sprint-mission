package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    void createUser(String username);

    //읽기
    User getUserById(UUID id);
    User getUserByName(String username);

    //모두읽기
    List<User> getAllUsers();

    //수정
    User updateUserName(UUID id, String username);

    //삭제
    void deleteUser(UUID id);

    boolean isDeleted(UUID id);
}
