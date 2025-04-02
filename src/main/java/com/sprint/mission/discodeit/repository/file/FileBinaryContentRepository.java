package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final FileManager fileManager;

    @Override
    public void save(BinaryContent binaryContent) {
        fileManager.writeToFile(SubDirectory.BINARY_CONTENT, binaryContent, binaryContent.getId());
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentUUID) {
        return fileManager.readFromFileById(SubDirectory.BINARY_CONTENT, binaryContentUUID, BinaryContent.class);
    }

    @Override
    public List<BinaryContent> findAll() {
        return fileManager.readFromFileAll(SubDirectory.BINARY_CONTENT, BinaryContent.class);
    }

    @Override
    public void delete(UUID binaryContentUUID) {
        fileManager.deleteFileById(SubDirectory.BINARY_CONTENT, binaryContentUUID);
    }
}
