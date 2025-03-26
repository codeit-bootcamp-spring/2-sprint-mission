package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.annotation.PostConstruct;
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

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    @Value("${discodeit.repository.file-directory}")
    private Path directory;
    private final String EXTENSION = ".ser";

    private String fileDirectory;

    @PostConstruct
    public void init() {
        // 현재 작업 디렉토리 하위에 fileDirectory 및 "UserStatus" 폴더 생성
        directory = Paths.get(System.getProperty("user.dir"), fileDirectory, UserStatus.class.getSimpleName());
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directory, e);
        }
    }

    // 주어진 id에 해당하는 파일 경로 생성 (예: {directory}/{UUID}.ser)
    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + EXTENSION);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to read file: " + path, e);
                        }
                    })
                    .filter(us -> us.getId() != null && us.getId().equals(userId))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in directory: " + directory, e);
        }
    }

    @Override
    public List<UserStatus> findAll() {
        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to read file: " + path, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in directory: " + directory, e);
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        if (userStatus.getId() == null) {
            userStatus.setId(UUID.randomUUID());
        }
        Path path = resolvePath(userStatus.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save UserStatus: " + userStatus, e);
        }
    }

    @Override
    public void delete(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete UserStatus with id: " + userStatus.getId(), e);
        }
    }
}
