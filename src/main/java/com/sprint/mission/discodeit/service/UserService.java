package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserEntity> getUserByUsername(String username);
    Optional<UserEntity> findById(UUID id);
    UserEntity updateUsername(UUID userId, String newUsername);//동명이인을 고려해서 userId 사용.
    UserEntity updateNickname(String nickname, String newNickname);
    UserEntity updatePhoneNumber(String phoneNumber, String newPhoneNumber);
    UserEntity updateEmail(String email, String newEmail);
    UserEntity updatePassword(String password, String newPassword);


}
