package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.config.FilePath.USER_FILE;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    @Override
    public User save(User requestUser) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        users.put(requestUser.getId(), requestUser);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);

        return requestUser;
    }

    @Override
    public User findById(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.get(id);
    }

    @Override
    public List<User> findByName(String name) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .filter(user -> user.isSameName(name))
                .toList();
    }

    @Override
    public User findByEmail(String email) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        User user = users.get(id);
        user.updateName(name);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        users.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);
    }
}
