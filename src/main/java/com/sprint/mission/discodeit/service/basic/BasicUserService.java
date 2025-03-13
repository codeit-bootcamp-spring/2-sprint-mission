package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    private void saveUser() {
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
        saveUser();
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = userRepository.findUserById(userID);
        user.addJoinedChannel(channelId);
        saveUser();
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public void removeChannel(UUID userId, UUID channelId) {
        User user = userRepository.findUserById(userId);
        user.removeJoinedChannel(channelId);
        saveUser();
    }

    @Override
    public void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
    }
}
