package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String BINARY_CONTENT_FILE;
    private final Map<UUID, BinaryContent> binaryContentData;
    private final SaveLoadHandler<BinaryContent> saveLoadHandler;


    public FileBinaryContentRepository(@Value("${discodeit.repository.file.binaryContent}") String fileName, SaveLoadHandler<BinaryContent> saveLoadHandler) {
        this.BINARY_CONTENT_FILE = fileName;
        this.saveLoadHandler = saveLoadHandler;
        binaryContentData = saveLoadHandler.loadData(BINARY_CONTENT_FILE);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent.getId(), binaryContent);
        saveLoadHandler.saveData(BINARY_CONTENT_FILE, binaryContentData);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentData.get(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentData.values().stream().filter(binaryContent -> ids.contains(binaryContent.getId())).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        binaryContentData.remove(id);
        saveLoadHandler.saveData(BINARY_CONTENT_FILE, binaryContentData);
    }
}
