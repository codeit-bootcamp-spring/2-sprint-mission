package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path binaryContentDirectory;

    public FileBinaryContentRepository() {
        this.binaryContentDirectory = FileUtils.baseDirectory.resolve("binaryContents");
        FileUtils.initializeDirectory(binaryContentDirectory);
    }

    @Override
    public UUID save(BinaryContent binaryContent) {
        Path binaryFile = binaryContentDirectory.resolve(
            binaryContent.getId().toString() + ".binaryContent");
        FileUtils.save(binaryFile, binaryContent);

        return binaryContent.getId();
    }

    @Override
    public BinaryContent find(UUID id) {
        Path binaryContentFile = binaryContentDirectory.resolve(id.toString() + ".channel");
        return Optional.ofNullable((BinaryContent) FileUtils.loadById(binaryContentFile))
            .orElseThrow(
                () -> new IllegalArgumentException("[ERROR]유효 하지 않은 아이디 입니다. id : " + id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileUtils.load(binaryContentDirectory);
    }


    @Override
    public void delete(UUID profileImageId) {
        Path binaryContentFile = binaryContentDirectory.resolve(
            profileImageId.toString() + ".binaryContent");
        FileUtils.delete(binaryContentFile);
    }
}
