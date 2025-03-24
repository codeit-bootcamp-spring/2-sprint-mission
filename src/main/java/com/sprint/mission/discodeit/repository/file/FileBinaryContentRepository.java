package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String fileName = "binaryContent.ser";
    private final Map<UUID, BinaryContent> binaryContentMap;
    private final FileDataManager fileDataManager;

    public FileBinaryContentRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.binaryContentMap = loadBinaryContentList();
    }

    private Map<UUID, BinaryContent> loadBinaryContentList() {
        Map<UUID, BinaryContent> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    private void saveBinaryContentList() {
        fileDataManager.saveObjectToFile(binaryContentMap);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.binaryContentMap.put(binaryContent.getId(), binaryContent);
        saveBinaryContentList();
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentIds.stream()
                .map(binaryContentMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryContentMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = binaryContentMap.remove(id) != null;
        if (removed) {
            saveBinaryContentList();
        }
    }
}
