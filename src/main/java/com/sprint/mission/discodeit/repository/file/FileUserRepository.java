package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final Path userPath;

    public FileUserRepository(Path userPath) {
        this.userPath = userPath;
    }

    @Override
    public User save(User requestUser) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        users.put(requestUser.getId(), requestUser);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), userPath, users);

        return requestUser;
    }

    @Override
    public User findById(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.get(id);
    }

    @Override
    public List<User> findByName(String name) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.values()
                .stream()
                .filter(user -> user.isSameName(name))
                .toList();
    }

    @Override
    public User findByEmail(String email) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        return users.values()
                .stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst()
                .orElse(null);
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

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), userPath, users);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(userPath);

        users.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), userPath, users);
    }
}
