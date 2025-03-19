package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@Qualifier("fileBinaryContentRepository")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            BinaryContent.class.getSimpleName());

    public FileBinaryContentRepository() {
        FileUtil.init(DIRECTORY);
    }

    private Path FileTypeDetector(BinaryContent binaryContent) {
        if (binaryContent.getType().equals(BinaryContentType.USER_PROFILE)) {
            return DIRECTORY;
        }
        return DIRECTORY;
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent, MultipartFile file) {
        Path directory = FileTypeDetector(binaryContent);
        FileUtil.save(directory, binaryContent, binaryContent.getId());
        FileUtil.saveBinaryContent(directory.resolve("File"), file, binaryContent.getId());

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.delete(DIRECTORY, id);
        FileUtil.delete(DIRECTORY.resolve("File"), id);
    }

}
