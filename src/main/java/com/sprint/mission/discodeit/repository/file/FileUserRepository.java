package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private static volatile FileUserRepository instance;
    private final Path directory;

    private FileUserRepository(Path directory) {
        this.directory = directory;
        init(directory);
    }

    public static FileUserRepository getInstance(Path directory) {
        if (instance == null) {
            synchronized (FileUserRepository.class) {
                if (instance == null) {
                    instance = new FileUserRepository(directory);
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
    public void save(User user) {
        Path filePath = getFilePath(user.getId());
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Path filePath = getFilePath(userId);
        if (Files.exists(filePath)) {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
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
    public void update(UUID id, String nickname, String email, String password) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.update(nickname, email, password, System.currentTimeMillis());
            save(user);
        } else {
            throw new NoSuchElementException("User not found with id: " + id);
        }
    }

    private static <T> List<T> load(Path directory) {
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
