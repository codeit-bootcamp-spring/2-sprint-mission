package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.SER_EXTENSION;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    @Value("${discodeit.repository.file-directory.user-path}")
    private Path userPath = STORAGE_DIRECTORY.resolve("user" + SER_EXTENSION);

    @Override
    public User save(User requestUser) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);
        users.put(requestUser.getId(), requestUser);

        saveObjectsToFile(STORAGE_DIRECTORY, userPath, users);

        return requestUser;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.values()
                .stream()
                .filter(user -> user.isSameName(name))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.values()
                .stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.values()
                .stream()
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        User user = users.get(id);
        user.updateName(name);

        saveObjectsToFile(STORAGE_DIRECTORY, userPath, users);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        users.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY, userPath, users);
    }
}
