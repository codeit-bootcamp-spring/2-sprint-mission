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
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path directory;
    private final Map<UUID, BinaryContent> binaryContentMap;
    private final FileRepository<BinaryContent> fileRepository;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String fileDir, FileRepository<BinaryContent> fileRepository) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "binarycontents");
        SerializationUtil.init(directory);
        this.fileRepository = fileRepository;
        this.binaryContentMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        fileRepository.saveToFile(binaryContent, directory);
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
        fileRepository.deleteFileById(id, directory);
        binaryContentMap.remove(id);
    }

    private void loadCacheFromFile() {
        List<BinaryContent> binaryContents = fileRepository.loadAllFromFile(directory);
        for(BinaryContent binaryContent : binaryContents) {
            binaryContentMap.put(binaryContent.getId(), binaryContent);
        }
    }
}
