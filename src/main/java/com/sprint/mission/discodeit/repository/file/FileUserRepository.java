package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("fileUserRepository")
public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            User.class.getSimpleName());
    private final String EXTENSION = ".ser";

    public FileUserRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public User save(User user) {
        return FileUtil.save(DIRECTORY, user, user.getId());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id);
    }

    @Override
    public List<User> findAll() {
        return FileUtil.findAll(DIRECTORY);
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.delete(DIRECTORY, id);
    }
}
