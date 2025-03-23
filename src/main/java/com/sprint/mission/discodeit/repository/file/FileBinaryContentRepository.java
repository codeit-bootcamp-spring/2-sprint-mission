package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository implements BinaryContentRepository {
    @Value("${discodeit.repository.file-directory}")
    private Path directory;
    private final String EXTENSION = ".ser";


    private String fileDirectory;

    @PostConstruct
    public void init() {
        // 현재 작업 디렉토리 하위에 fileDirectory와 BinaryContent 클래스 이름의 폴더 생성
        directory = Paths.get(System.getProperty("user.dir"), fileDirectory, BinaryContent.class.getSimpleName());
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directory, e);
        }
    }

    // 각 BinaryContent 객체의 파일 경로를 생성: {directory}/{UUID}.ser
    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + EXTENSION);
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID id) {
        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (BinaryContent) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to read file: " + path, e);
                        }
                    })
                    .filter(bc -> bc.getUserId() != null && bc.getUserId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in directory: " + directory, e);
        }
    }

    @Override
    public List<BinaryContent> findAll() {
        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (BinaryContent) ois.readObject();
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
    public List<BinaryContent> findAllById(List<UUID> ids) {
        // File 기반에서는 findAll() 후 필터링하는 방식으로 구현
        return findAll().stream()
                .filter(bc -> ids.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(BinaryContent binaryContent) {
        if (binaryContent.getId() == null) {
            binaryContent.setId(UUID.randomUUID());
        }
        Path path = resolvePath(binaryContent.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save BinaryContent: " + binaryContent, e);
        }
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete BinaryContent with id: " + binaryContent.getId(), e);
        }
    }
}
