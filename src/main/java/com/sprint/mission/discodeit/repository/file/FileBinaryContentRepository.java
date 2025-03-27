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
        binaryContentMap.put(content.getId(), content);
        save();
    }

    @Override
    public BinaryContent findBinaryContentById(UUID id) {
        return binaryContentMap.get(id);
    }


    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void deleteBinaryContentById(UUID contentId) {
        binaryContentMap.remove(contentId);
        save();
    }

    @Override
    public boolean existsBinaryContent(UUID contentId) {
        return binaryContentMap.containsKey(contentId);
    }
}
