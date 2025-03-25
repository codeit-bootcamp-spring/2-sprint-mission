package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path DIRECTORY;

    public FileBinaryContentRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getBinaryContent());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createBinaryContent(BinaryContent binaryContent) {
        // 중복 여부 확인
//        if (binaryContentRepository.existsByFileHashAndFileSize(createDto.fileHash(), createDto.fileSize())) {
//            throw new IllegalArgumentException("이미 존재하는 파일입니다.");
//        }
        String fileName = binaryContent.getFileName();
        FileUtil.saveBytesToFile(binaryContent.getFileData(), DIRECTORY, fileName);
        return binaryContent.getId();
    }

    @Override
    public BinaryContent findById(UUID id) {
        return (BinaryContent) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(BinaryContent.class::isInstance)
                .map(BinaryContent.class::cast)
                .filter(object -> idList.contains(object.getId()))
                //.filter(binaryContent -> idSet.contains(binaryContent.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        checkBinaryContentExists(id);

        FileUtil.deleteFile(DIRECTORY, id);
    }

    @Override
    public List<byte[]> findAll() {
        return FileUtil.loadAllByteFiles(DIRECTORY).stream()
                .peek(fileData -> System.out.println("검증: " + Arrays.toString(fileData)))
                .collect(Collectors.toList());
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkBinaryContentExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id);
        }
    }

}
