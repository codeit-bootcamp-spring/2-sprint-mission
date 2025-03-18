package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String FILE_PATH = "src/main/resources/binaryContent.dat";
    private static Map<UUID, BinaryContent> binaryContentMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    public FileBinaryContentRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        binaryContentMap = fileStorageManager.loadFile(FILE_PATH);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(FILE_PATH, binaryContentMap);
    }

    @Override
    public void addBinaryContent(BinaryContent content) {
        binaryContentMap.put(content.id(), content);
    }

    @Override
    public BinaryContent findBinaryContent(UUID binaryContentId) {
        return binaryContentMap.get(binaryContentId);
    }

    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        binaryContentMap.remove(binaryContentId);
    }

    @Override
    public boolean existsBinaryContent(UUID binaryContentId) {
        return binaryContentMap.containsKey(binaryContentId);
    }
}
