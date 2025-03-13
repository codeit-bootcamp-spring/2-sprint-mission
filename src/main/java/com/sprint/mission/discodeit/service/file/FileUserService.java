package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FileUserService implements UserService {

    private static FileUserService INSTANCE;
    private final FileUserRepository fileUserRepository;

    private FileUserService(FileUserRepository fileUserRepository) {
        this.fileUserRepository = fileUserRepository;
    }

    public static synchronized FileUserService getInstance(FileUserRepository fileUserRepository) {
        if (INSTANCE == null) {
            INSTANCE = new FileUserService(fileUserRepository);
        }
        return INSTANCE;
    }

    private void saveUserData() {
        fileUserRepository.save();
    }

    @Override
    public User createUser(String username) {
        User user = new User(username);
        fileUserRepository.addUser(user);
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        validateUserExists(userId);
        return fileUserRepository.findUserById(userId);
    }

    @Override
    public List<User> findUsersByIds(Set<UUID> userIds) {
        return List.of();
    }

    @Override
    public List<User> getAllUsers() {
        return fileUserRepository.findUserAll();
    }

    @Override
    public String getUserNameById(UUID userId) {
        validateUserExists(userId);
        return fileUserRepository.findUserById(userId).getUsername();
    }

    @Override
    public void updateUsername(UUID userID, String newUsername) {
        User user = getUserById(userID);
        user.updateUsername(newUsername);
        saveUserData();
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);

        if (user.isJoinedChannel(channelId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 채널입니다. ");
        }

        user.addJoinedChannel(channelId);
        saveUserData();
    }

    @Override
    public void deleteUser(UUID userID) {
        fileUserRepository.deleteUserById(userID);
    }

    @Override
    public void removeChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);
        user.removeJoinedChannel(channelId);
        saveUserData();
    }


    public void validateUserExists(UUID userId) {
        if (!fileUserRepository.existsById(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
