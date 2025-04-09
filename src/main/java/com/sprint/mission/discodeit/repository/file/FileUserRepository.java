package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository implements UserRepository {

    private final Path userDirectory;

    public FileUserRepository() {
        this.userDirectory = FileUtils.baseDirectory.resolve("users");
        FileUtils.initializeDirectory(userDirectory);
    }

    @Override
    public void save(User user) {
        Path userFile = userDirectory.resolve(user.getId().toString() + ".user");
        FileUtils.save(userFile, user);
    }

    @Override
    public User findByUserId(UUID userId) {
        Path userFile = userDirectory.resolve(userId.toString() + ".user");
        return (User) FileUtils.loadById(userFile);
    }

    @Override
    public User findByUserName(String name) {
        for (User user : findAll()) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User user : findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return FileUtils.load(userDirectory);
    }

    @Override
    public void delete(UUID userId) {
        Path userFile = userDirectory.resolve(userId.toString() + ".user");
        FileUtils.delete(userFile);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream()
            .anyMatch(u -> u.getName().equals(username));
    }

    @Override
    public boolean existByEmail(String email) {
        return findAll().stream()
            .anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public boolean existById(UUID userId) {
        return findAll().stream()
            .anyMatch(user -> user.getId().equals(userId));
    }
}
