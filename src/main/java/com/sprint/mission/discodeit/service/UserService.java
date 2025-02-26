package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    void createUser(String userName, String userEmail, String password);
    User readUser(UUID id);
    Set<User> readAllUsers();
    void updateUserName(UUID id, String newUserName);
    void updatePassword(UUID id, String newPassword);
    void deleteUser(UUID id);
    static void validateUserId(UUID userId, UserRepository userRepository) {
        if (userId == null) {
            throw new IllegalArgumentException("null 인 id 값이 들어왔습니다!!!");
        }
        userRepository.findUserById(userId);
    }
}