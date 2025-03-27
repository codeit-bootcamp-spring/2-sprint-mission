package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileUserRepository implements UserRepository {

    private final FileManager fileManager;

    @Override
    public User save(User user) {
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
    public Optional<User> findUserByEmail(String email) {
        return findAllUser().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = fileManager.readFromFileAll(SubDirectory.USER, User.class);
        return userList;
    }

    @Override
    public void delete(UUID userUUID) {
        fileManager.deleteFileById(SubDirectory.USER, userUUID);
    }
}
