package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, UserStatus.class.getSimpleName());
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
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 저장 실패: " + userStatus.getId(), e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                UserStatus userStatus = (UserStatus) ois.readObject();
                return Optional.of(userStatus);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("UserStatus 읽기 실패: " + userStatusId, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("UserStatus 읽기 실패: " + path, e);
                        }
                    })
                    .filter(userStatus -> userStatus.getUserId().equals(userId))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 목록 로딩 실패", e);
        }
    }

    @Override
    public List<UserStatus> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("UserStatus 읽기 실패: " + path, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 목록 로딩 실패", e);
        }
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return Files.exists(resolvePath(userStatusId));
    }

    @Override
    public void deleteById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 삭제 실패: " + userStatusId, e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            List<Path> toDelete = paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .filter(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            UserStatus userStatus = (UserStatus) ois.readObject();
                            return userStatus.getUserId().equals(userId);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("UserStatus 읽기 실패: " + path, e);
                        }
                    })
                    .collect(Collectors.toList());
            for (Path path : toDelete) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 삭제 실패 for userId: " + userId, e);
        }
    }
}
