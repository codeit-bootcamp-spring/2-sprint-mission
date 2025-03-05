package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.DuplicatedUserException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private static volatile JCFUserService instance;

    private UserRepository userRepository;

    private JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static JCFUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService(userRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public User createUser(String nickname, String email, String avatar, String status) {
        validateUniqueEmail(email);
        return userRepository.save(new User(nickname, email, avatar, status));
    }

    @Override
    public User getUserByUserId(UUID userId) {
        validateUserId(userId);
        return userRepository.findById(userId);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UUID userId, String nickname, String avatar, String status) {
        User user = getUserByUserId(userId);
        user.update(nickname, avatar, status);
        return user;
    }


    @Override
    public void deleteUserById(UUID userId) {
        validateUserId(userId);
        userRepository.delete(userId);
    }

    @Override
    public void validateUserId(UUID userId) {
        if (!userRepository.exists(userId)) {
            throw new UserNotFoundException("해당 유저가 없습니다.");
        }
    }

    private void validateUniqueEmail(String email) {
        if (hasUserByEmail(email)) {
            throw new DuplicatedUserException("이메일 중복입니다.");
        }
    }

    private boolean hasUserByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

}
