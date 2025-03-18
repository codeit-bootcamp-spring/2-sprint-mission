package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + DIRECTORY, e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    @Override
    public User save(User user) {
        Path path = resolvePath(user.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 실패: " + user.getId(), e);
        }
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                User user = (User) ois.readObject();
                return Optional.of(user);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("유저 읽기 실패: " + id, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("유저 읽기 실패: " + path, e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("유저 목록 로딩 실패: " + DIRECTORY, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new RuntimeException("유저 삭제 실패: " + id, e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
