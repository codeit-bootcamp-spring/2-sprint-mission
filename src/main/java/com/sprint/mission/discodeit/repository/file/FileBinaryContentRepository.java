package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getBinaryContent());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        String extension = getFileExtension(binaryContent.getFileName());
        String fileName = binaryContent.getId().toString() + extension;
        FileUtil.saveBytesToFile(binaryContent.getFileData(), DIRECTORY, fileName);
        return binaryContent;
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return FileUtil.loadFromFile(DIRECTORY, id);
    }

    public Optional<byte[]> findBinaryById(UUID id) {
        return FileUtil.loadBinaryFile2(DIRECTORY, id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
                .filter(BinaryContent.class::isInstance)
                .map(BinaryContent.class::cast)
                .filter(object -> idList.contains(object.getId()))
                //.filter(binaryContent -> idSet.contains(binaryContent.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.deleteFile(DIRECTORY, id);
    }

    @Override
    public List<byte[]> findAll() {
        return new ArrayList<>(FileUtil.loadAllByteFiles(DIRECTORY));
    }


}
