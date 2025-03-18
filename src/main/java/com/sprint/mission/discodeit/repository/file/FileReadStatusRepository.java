package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, ReadStatus.class.getSimpleName());
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
    public ReadStatus save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 저장 실패: " + readStatus.getId(), e);
        }
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        Path path = resolvePath(readStatusId);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                ReadStatus readStatus = (ReadStatus) ois.readObject();
                return Optional.of(readStatus);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("ReadStatus 읽기 실패: " + readStatusId, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (ReadStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("ReadStatus 읽기 실패: " + path, e);
                        }
                    })
                    .filter(readStatus -> readStatus.getUserId().equals(userId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 목록 로딩 실패", e);
        }
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (ReadStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("ReadStatus 읽기 실패: " + path, e);
                        }
                    })
                    .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 목록 로딩 실패", e);
        }
    }

    @Override
    public boolean existsById(UUID readStatusId) {
        return Files.exists(resolvePath(readStatusId));
    }

    @Override
    public void deleteById(UUID readStatusId) {
        Path path = resolvePath(readStatusId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 삭제 실패: " + readStatusId, e);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            ReadStatus readStatus = (ReadStatus) ois.readObject();
                            if (readStatus.getChannelId().equals(channelId)) {
                                Files.deleteIfExists(path);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("ReadStatus 삭제 실패: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("채널에 해당하는 ReadStatus 삭제 실패", e);
        }
    }
}
