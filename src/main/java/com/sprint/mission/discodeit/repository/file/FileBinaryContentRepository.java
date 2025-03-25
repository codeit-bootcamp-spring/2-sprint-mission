package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "file",
        matchIfMissing = true)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String fileName = "binaryContent.dat";
    private static Map<UUID, BinaryContent> binaryContentMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    public FileBinaryContentRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        binaryContentMap = fileStorageManager.loadFile(fileName);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(fileName, binaryContentMap);
    }

    @Override
    public void addBinaryContent(BinaryContent content) {
        binaryContentMap.put(content.id(), content);
        save();
    }

    @Override
    public BinaryContent findBinaryContentById(UUID id) {
        return binaryContentMap.get(id);
    }

    @Override
    public BinaryContent findBinaryContentByUserId(UUID referenceId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.id().equals(referenceId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BinaryContent> findBinaryContentByMessageId(UUID referenceId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.id().equals(referenceId))
                .toList();
    }

    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void deleteBinaryContent(UUID referenceId) {
        binaryContentMap.values()
                .removeIf(content -> content.id().equals(referenceId));
        save();
    }

    @Override
    public boolean existsBinaryContent(UUID referenceId) {
        return binaryContentMap.containsKey(referenceId);
    }
}
