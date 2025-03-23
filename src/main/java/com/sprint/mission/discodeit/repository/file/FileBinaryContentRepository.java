package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String BINARY_CONTENT_FILE = "binaryContents.ser";
    private final Map<UUID, BinaryContent> binaryContentData;
    private final SaveLoadHandler<BinaryContent> saveLoadHandler;

    public FileBinaryContentRepository() {
        saveLoadHandler = new SaveLoadHandler<>(BINARY_CONTENT_FILE);
        binaryContentData = saveLoadHandler.loadData();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent.getId(), binaryContent);
        saveLoadHandler.saveData(binaryContentData);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentData.get(id);
    }

    @Override
    public void delete(UUID id) {
        binaryContentData.remove(id);
        saveLoadHandler.saveData(binaryContentData);
    }
}
