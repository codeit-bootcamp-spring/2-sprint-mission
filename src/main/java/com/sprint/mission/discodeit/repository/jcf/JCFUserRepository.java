package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFUserRepository extends AbstractRepository<User> implements UserRepository {
    private Map<String, User> usernames;
    private Map<String, User> emails;

    private JCFUserRepository() {
        super(User.class, new ConcurrentHashMap<>());
        usernames = new HashMap<>();
        emails = new HashMap<>();
    }

    @Override
    public void add(User newUser) {
        super.add(newUser);
        usernames.put(newUser.getUserName(), newUser);
        emails.put(newUser.getUserEmail(), newUser);
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        User findUser = super.findById(userId);
        findUser.updateUserName(newUserName);
        usernames.remove(findUser.getUserName());
        usernames.put(newUserName, findUser);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserPassword(newPassword);
        }
    }

    @Override
    public void updateProfileId(UUID userId, UUID newProfileId) {
        User findUser = super.findById(userId);
        findUser.updateProfileId(newProfileId);
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (userName == null) {
            throw new NullPointerException("userName is null");
        }
        return usernames.containsKey(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) {
            throw new NullPointerException("email is null");
        }
        return emails.containsKey(email);
    }

    @Override
    public User findByUserName(String userName) {
        if (!existsByUserName(userName)) {
            throw new NoSuchElementException("해당 userName을 가진 user 가 존재하지 않습니다.");
        }
        return usernames.get(userName);
    }

    @Override
    public User findByEmail(String email) {
        if (!existsByEmail(email)) {
            throw new NoSuchElementException("해당 email을 가진 user 가 존재하지 않습니다.");
        }
        return emails.get(email);
    }

    @Override
    public void deleteById(UUID userId) {
        super.deleteById(userId);
        usernames.remove(super.findById(userId).getUserName());
        emails.remove(super.findById(userId).getUserName());
    }
}
