package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface UserService {
    UUID createUser();

    void searchUser(UUID id);

    void searchAllUsers();

    void updateUser(UUID id);

    void deleteUser(UUID id);
}
