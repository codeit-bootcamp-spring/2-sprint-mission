package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserEntity> getUserByUsername(String username);
    UserEntity updateUsername(String username, String newUsername);
    UserEntity updateNickname(String nickname, String newNickname);
    UserEntity updatePhoneNumber(String phoneNumber, String newPhoneNumber);
    UserEntity updateEmail(UUID email, String newEmail);
    UserEntity updatePassword(UUID password, String newPassword);
    //각각의 변수로 식별하는 게 옳은건지 ?


}
