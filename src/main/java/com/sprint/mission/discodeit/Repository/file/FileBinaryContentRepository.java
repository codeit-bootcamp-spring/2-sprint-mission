package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.Exception.CommonException;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final FileRepositoryImpl<List<BinaryContent>> fileRepository;
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "BinaryContentList.ser");
    private List<BinaryContent> binaryContentList = new LinkedList<>();

    public FileBinaryContentRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        this.fileRepository.load();
    }

    @Override
    public void save(BinaryContentDTO binaryContentDTO) {
        binaryContentList.add(binaryContentDTO.binaryContent());
        fileRepository.save(binaryContentList);
    }

    @Override
    public BinaryContent find(UUID binaryId) {
        BinaryContent content = CommonUtils.findById(binaryContentList, binaryId, BinaryContent::getBinaryContentId)
                .orElseThrow(() -> CommonExceptions.BINARY_CONTENT_NOT_FOUND);
        return content;
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        if (binaryContentList.isEmpty()) {
            throw CommonExceptions.EMPTY_BINARY_CONTENT_LIST;
        }
        return binaryContentList;
    }

    @Override
    public boolean delete(UUID binaryId) {
        try {
            BinaryContent content = find(binaryId);
            binaryContentList.remove(content);
            fileRepository.save(binaryContentList);
            return true;
        } catch (CommonException e) {
            System.out.println("해당 바이너리 데이터는 존재하지 않습니다.");
            return false;
        }
    }
}
