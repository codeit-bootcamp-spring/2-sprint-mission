package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BasicUserService implements UserService {

    private static BasicUserService INSTANCE;
    private final UserRepository userRepository;

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static synchronized BasicUserService getInstance(UserRepository userRepository) {
        if (INSTANCE == null) {
            INSTANCE = new BasicUserService(userRepository);
        }
        return INSTANCE;
    }

    public void saveUserData() {
        userRepository.save();
    }

    @Override
    public User createUser(String username) {
        User user = new User(username);
        userRepository.addUser(user);
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public String getUserNameById(UUID userId) {
        return userRepository.findUserById(userId).getUsername();
    }

    @Override
    public List<User> findUsersByIds(Set<UUID> userIds) {
        return userRepository.findUsersByIds(userIds);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findUserAll();
    }

    @Override
    public void updateUsername(UUID userId, String newUsername) {
        User user = userRepository.findUserById(userId);
        user.updateUsername(newUsername);
        userRepository.addUser(user);
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = userRepository.findUserById(userID);
        user.addJoinedChannel(channelId);
        userRepository.addUser(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public void removeChannel(UUID userId, UUID channelId) {
    User user = userRepository.findUserById(userId);
        user.removeJoinedChannel(channelId);
    userRepository.addUser(user);
    }

    @Override
    public void validateUserExists(UUID userId) {
        if(!userRepository.existsById(userId)){
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
    }
}
