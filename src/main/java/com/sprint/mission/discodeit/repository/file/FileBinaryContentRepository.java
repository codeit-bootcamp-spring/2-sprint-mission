package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;

    public FileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory:file-data-map}") String fileDirectory
    ) {
        DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
                BinaryContent.class.getSimpleName());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        FileUtil.save(DIRECTORY, binaryContent, binaryContent.getId());
        return binaryContent;
    }

    @Override
    public String saveFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String uuidName = UUID.randomUUID() + "_" + originalName;

        return FileUtil.saveFile(DIRECTORY, uuidName, file);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id, BinaryContent.class);
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    @Override
    public void deleteById(BinaryContent binaryContent) {
        Path filePath = Paths.get(binaryContent.getFilePath());
        FileUtil.deleteFile(filePath);
        FileUtil.delete(DIRECTORY, binaryContent.getId());
    }

}
