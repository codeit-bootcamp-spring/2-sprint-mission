package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.SER_EXTENSION;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadAndSaveConsumer;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

    @Value("${discodeit.repository.file-directory.binary-content-path}")
    private Path binaryContentPath = STORAGE_DIRECTORY.resolve("binaryContent" + SER_EXTENSION);

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        loadAndSaveConsumer(binaryContentPath, (Map<UUID, BinaryContent> binaryContents) ->
                binaryContents.put(binaryContent.getId(), binaryContent)
        );

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        Map<UUID, BinaryContent> binaryContents = loadObjectsFromFile(binaryContentPath);
        return Optional.ofNullable(binaryContents.get(uuid));
    }

    @Override
    public void delete(UUID id) {
        loadAndSaveConsumer(binaryContentPath, (Map<UUID, BinaryContent> binaryContents) ->
                binaryContents.remove(id)
        );
    }
}
