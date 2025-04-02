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

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path binaryContentPath;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory.binary-content-path}") Path binaryContentPath) {
        this.binaryContentPath = binaryContentPath;
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        loadAndSave(binaryContentPath, (Map<UUID, BinaryContent> binaryContents) ->
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
        loadAndSave(binaryContentPath, (Map<UUID, BinaryContent> binaryContents) ->
                binaryContents.remove(id)
        );
    }
}
