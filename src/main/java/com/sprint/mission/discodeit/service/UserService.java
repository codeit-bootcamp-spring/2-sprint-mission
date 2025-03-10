package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.Set;
import java.util.UUID;

//유저 CRUD
public interface UserService {
    void createdUser(String email, String password); //등록
    User findByUserId(UUID userId); //조회(단건)
    Set<UUID> findByAllUsersId();//모든 유저조회
    User deleteUser(UUID userId);//삭제
    
    User updateEmail(UUID userId, String newEmail); //이메일 업데이트
    User updatePassword(UUID userId, String newPassword); //비밀번호 업데이트
}
