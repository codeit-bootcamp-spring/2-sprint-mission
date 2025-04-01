package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:file-data-map}") String fileDirectory
    ) {
        DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
                User.class.getSimpleName());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public User save(User user) {
        return FileUtil.save(DIRECTORY, user, user.getId());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id, User.class);
    }

    @Override
    public List<User> findAll() {
        return FileUtil.findAll(DIRECTORY, User.class);
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

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();
    }
}
