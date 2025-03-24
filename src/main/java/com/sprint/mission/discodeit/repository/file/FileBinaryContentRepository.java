package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.BINARY_CONTENT_TEST_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private Path binaryContentPath = BINARY_CONTENT_TEST_FILE;

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> binaryContents = loadObjectsFromFile(binaryContentPath);
        binaryContents.put(binaryContent.getId(), binaryContent);

        saveObjectsToFile(STORAGE_DIRECTORY, binaryContentPath, binaryContents);

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        Map<UUID, BinaryContent> binaryContents = loadObjectsFromFile(binaryContentPath);
        return Optional.ofNullable(binaryContents.get(uuid));
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, BinaryContent> binaryContents = loadObjectsFromFile(binaryContentPath);
        binaryContents.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY, binaryContentPath, binaryContents);
    }
}
