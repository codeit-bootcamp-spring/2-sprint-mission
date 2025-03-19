package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements BinaryContentRepository {
    public FileBinaryContentRepository() {
        super(BinaryContent.class, Paths.get(System.getProperty("BinaryContent.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\BinaryContentData"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    @Override
    public void add(BinaryContent newBinaryContent) {
        super.add(newBinaryContent);
        super.saveToFile(directory.resolve(newBinaryContent.getId().toString() + ".ser"), newBinaryContent);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> responses = new ArrayList<>();
        for(UUID id : ids) {
            BinaryContent binaryContent = findById(id);
            responses.add(binaryContent);
        }
        return responses;
    }

    @Override
    public void deleteById(UUID binaryContentId) {
        super.deleteById(binaryContentId);
        super.deleteFile(binaryContentId);
    }
}
