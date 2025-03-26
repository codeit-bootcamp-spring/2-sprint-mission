package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path directoryPath;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String directoryPath) {
        this.directoryPath = Paths.get(System.getProperty("user.dir"), directoryPath, "userStatuses");
        init();
    }

    private void init() {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path filePath = getFilePath(userStatus.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(userStatus);
            return userStatus;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " UserStatus 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID userStatusId) {
        return directoryPath.resolve(userStatusId + ".ser");
    }

    @Override
    public List<UserStatus> findAll() {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("UserStatuses 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private UserStatus readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (UserStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " UserStatus 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        Path filePath = getFilePath(userStatusId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("UserStatus ID " + userStatusId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths.map(this::readUserFromFile)
                    .filter(userStatus ->
                            userStatus.getUserId().equals(userId)
                    )
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("UserStatuses 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public void delete(UUID userStatusId) {
        Path filePath = getFilePath(userStatusId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " UserStatus 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
