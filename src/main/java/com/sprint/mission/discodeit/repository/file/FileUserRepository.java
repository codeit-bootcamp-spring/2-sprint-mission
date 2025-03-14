package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
@RequiredArgsConstructor
public class FileUserRepository implements UserRepository {

    private final FileManager fileManager;

    @Override
    public User save(String username, String password, String nickname, String profile) {
        User user = new User(username, password, nickname, profile);
        fileManager.writeToFile(SubDirectory.USER, user, user.getId());
        return user;
    }

    @Override
    public Optional<User> findUserById(UUID userUUID) {
        Optional<User> user = fileManager.readFromFileById(SubDirectory.USER, userUUID, User.class);
        return user;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return findAllUser().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = fileManager.readFromFileAll(SubDirectory.USER, User.class);
        return userList;
    }

    @Override
    public User updateUserNickname(UUID userUUID, String nickname) {
        User user = findUserById(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.: " + userUUID));
        user.updateNickname(nickname);
        fileManager.writeToFile(SubDirectory.USER, user, user.getId());
        return user;
    }

    @Override
    public boolean deleteUserById(UUID userUUID) {
        return fileManager.deleteFileById(SubDirectory.USER, userUUID);
    }
}
