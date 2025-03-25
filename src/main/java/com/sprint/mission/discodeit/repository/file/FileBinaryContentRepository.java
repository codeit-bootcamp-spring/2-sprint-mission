package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "binaryContents");

    private final FileSerializationUtil fileUtil;

    public FileBinaryContentRepository(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(BinaryContent binaryContent) {
        fileUtil.writeObjectToFile(binaryContent, FilePathUtil.getFilePath(directory, binaryContent.getId()));
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return fileUtil.readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<BinaryContent>> findAll() {
        try {
            List<BinaryContent> binaryContents = Files.list(directory)
                    .map(fileUtil::<BinaryContent>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(binaryContents);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<BinaryContent>> findAllByIdIn(UUID userId) {
        try {
            List<BinaryContent> binaryContents = Files.list(directory)
                    .map(fileUtil::<BinaryContent>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(content -> content.getUserId().equals(userId))
                    .toList();
            return Optional.of(binaryContents);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(BinaryContent binaryContent) {
        save(binaryContent);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
