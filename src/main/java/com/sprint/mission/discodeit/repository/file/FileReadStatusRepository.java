package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
import org.springframework.stereotype.Repository;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data",
            "readStatuses");

    public FileReadStatusRepository() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path filePath = getFilePath(readStatus.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(readStatus);
            return readStatus;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " ReadStatus 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID readStatusId) {
        return DIRECTORY_PATH.resolve(readStatusId + ".ser");
    }

    @Override
    public List<ReadStatus> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("ReadStatuses 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private ReadStatus readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (ReadStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " ReadStatus 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
            return paths.map(this::readUserFromFile)
                    .filter(readStatus -> readStatus.getUserId().equals(userId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("ReadStatuses 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
            return paths.map(this::readUserFromFile)
                    .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("ReadStatuses 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        Path filePath = getFilePath(readStatusId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("ReadStatus ID " + readStatusId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public void delete(UUID readStatusId) {
        Path filePath = getFilePath(readStatusId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " ReadStatusId 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
