package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFUserRepository extends AbstractRepository<User> implements UserRepository {

    private JCFUserRepository() {
        super(User.class, new ConcurrentHashMap<>());
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserName(newUserName);
        }
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserPassword(newPassword);
        }
    }
}
