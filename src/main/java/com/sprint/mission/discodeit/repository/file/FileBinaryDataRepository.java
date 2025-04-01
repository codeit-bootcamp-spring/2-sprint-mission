package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileBinaryDataRepository implements BinaryDataRepository {

    private final FileManager fileManager;

    @Override
    public BinaryData save(BinaryData binaryData) {
        return fileManager.writeToFile(SubDirectory.BINARY_DATA, binaryData, binaryData.getId());
    }

    @Override
    public Optional<BinaryData> findById(UUID binaryContentUUID) {
        return fileManager.readFromFileById(SubDirectory.BINARY_DATA, binaryContentUUID, BinaryData.class);
    }
}
