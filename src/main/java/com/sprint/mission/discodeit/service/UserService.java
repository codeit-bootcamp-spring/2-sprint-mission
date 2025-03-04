package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 유저 생성
    void createUser(User user);

    // 유저 읽기
    // UUID id를 파라미터로 사용해 불러오기엔, UUID는 난수값이라 미리 알기가 힘들고 User DB를 따로 사용하지 않으므로 알길이 없음
    // => username으로 User를 찾는게 훨씬 편할 것 같다.
    User getUser(String username);

    // 유저 모두 읽기
    List<User> getAllUsers();

    // 유저 수정
    void updateUser(User user);

    // 권한 추가
    void addRole(String role, String username);

    // 권한 삭제
    void removeRole(String role, String username);

    // 유저 삭제
    void deleteUser(String username);

}
