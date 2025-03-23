package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");

    private final FileSerializationUtil fileUtil;

    public FileUserRepository(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }


    @Override
    public void save(User user) {
        fileUtil.<User>writeObjectToFile(user, FilePathUtil.getFilePath(directory, user.getId()));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return fileUtil.<User>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<User>> findAll() {
        try {
            List<User> users = Files.list(directory)
                    .map(fileUtil::<User>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(users);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(User user) {
        save(user);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return findAll()
                .flatMap(users -> users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll()
                .flatMap(users -> users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst());
    }
}
