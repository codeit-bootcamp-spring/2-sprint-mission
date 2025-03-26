package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository, FileRepository<BinaryContent> {
    private final Path directory;
    private final Map<UUID, BinaryContent> binaryContentMap;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String fileDir) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "binarycontents");
        SerializationUtil.init(directory);
        this.binaryContentMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        saveToFile(binaryContent);
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        deleteFileById(id);
        binaryContentMap.remove(id);
    }

    @Override
    public void saveToFile(BinaryContent binaryContent) {
        Path filePath = directory.resolve(binaryContent.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, binaryContent);
    }

    @Override
    public List<BinaryContent> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID id) {
        Path filePath = directory.resolve(id + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("binaryContent 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<BinaryContent> binaryContents = loadAllFromFile();
        for(BinaryContent binaryContent : binaryContents) {
            binaryContentMap.put(binaryContent.getId(), binaryContent);
        }
    }
}
