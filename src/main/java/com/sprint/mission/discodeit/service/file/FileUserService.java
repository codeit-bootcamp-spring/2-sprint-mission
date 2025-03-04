package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static volatile FileUserService instance;
    private final Path directory;

    private FileUserService(Path directory) {
        this.directory = directory;
        init(directory);
    }

    public static FileUserService getInstance(Path directory) {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService(directory);
                }
            }
        }
        return instance;
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
    public void create(User user) {
        save(getFilePath(user.getId()), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        Path filePath = getFilePath(id);
        if (Files.exists(filePath)) {
            try (
                    FileInputStream fis = new FileInputStream(filePath.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                return Optional.of((User) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return load(directory);
    }

    @Override
    public void delete(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID id, String nickname) {
        findById(id).ifPresent(user -> {
            user.setNickname(nickname, System.currentTimeMillis());
            save(getFilePath(id), user);
        });
    }

    private <T> void save(Path filePath, T data) {
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> load(Path directory) {
        if (Files.exists(directory)) {
            try {
                return Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (T) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    private Path getFilePath(UUID id) {
        return directory.resolve(id.toString().concat(".ser"));
    }
}
