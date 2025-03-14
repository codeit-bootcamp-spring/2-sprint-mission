package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public interface UserService {
    User createUser(UserCreateRequest userCreateRequest);
    UserReadResponse readUser(UUID id);
    Map<UUID, User> readAllUsers();
    void updateUserName(UUID id, String newUserName);
    void updatePassword(UUID id, String newPassword);
    void deleteUser(UUID id);
    static void validateUserId(UUID userId, UserRepository jcfUserRepository) {
        if (!jcfUserRepository.existsById(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 사용자를 찾을 수 없습니다 : " + userId);
        }
    }
}