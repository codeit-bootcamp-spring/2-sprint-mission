package com.sprint.discodeit.sprint4.service;

import com.sprint.discodeit.sprint5.domain.entity.users;

import java.util.List;
import java.util.UUID;

public interface usersService {
    users create(String usersname, String email, String password);
    users find(UUID usersId);
    List<users> findAll();
    users update(UUID usersId, String newusersname, String newEmail, String newPassword);
    void delete(UUID usersId);
}
