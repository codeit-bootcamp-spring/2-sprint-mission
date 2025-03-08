package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username) {
        User user = new User(username);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public String getUserNameById(UUID userId) {
        return userRepository.findById(userId).getUsername();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updataUserData() {

    }

    @Override
    public void updateUsername(UUID userId, String newUsername) {
        User user = userRepository.findById(userId);
        user.updateUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = userRepository.findById(userID);
        user.addJoinedChannel(channelId);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteChannel(UUID userId, UUID channelId) {
    User user = userRepository.findById(userId);
        user.removeJoinedChannel(channelId);
    userRepository.save(user);
    }

    @Override
    public void validateUserExists(UUID userId) {
        if(!userRepository.exists(userId)){
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
    }
}
