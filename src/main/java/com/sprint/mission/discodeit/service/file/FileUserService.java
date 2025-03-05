package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        Path directory = Paths.get(System.getProperty("user.dir"), "src/main/java/com/sprint/mission/discodeit/User");
        init(directory);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public User getUser(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUpdatedUsers() {
        return userRepository.findUpdatedUsers();
    }

    @Override
    public void registerUser(String userName, String nickName) {
        userRepository.createUser(userName, nickName);
    }

    @Override
    public void updateName(String oldUserName, String newUserName, String newNickName) {
        userRepository.updateUser(oldUserName, newUserName, newNickName);
    }

    @Override
    public void deleteUser(String userName) {
        userRepository.deleteUser(userName);
        // 해당 userName가지는 channel들 삭제 코드
        // 코드에 따라 message도 삭제하게 해야 함.
    }
}
