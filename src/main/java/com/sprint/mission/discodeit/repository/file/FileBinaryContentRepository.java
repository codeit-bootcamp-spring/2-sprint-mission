package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.exception.Empty.EmptyBinaryContentListException;
import com.sprint.mission.discodeit.exception.NotFound.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.util.CommonUtils;
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
        try {
            this.binaryContentList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileBinaryContentRepository init");
        }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentList.add(binaryContent);
        fileRepository.save(binaryContentList);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        BinaryContent content = CommonUtils.findById(binaryContentList, binaryId, BinaryContent::getId)
                .orElseThrow(() -> new BinaryContentNotFoundException("바이너리 데이터를 찾을 수 없습니다."));
        return content;
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        if (binaryContentList.isEmpty()) {
            throw new EmptyBinaryContentListException("Repository 내 바이너리 정보 리스트가 비어있습니다.");
        }
        return binaryContentList;
    }

    @Override
    public boolean delete(UUID binaryId) {
        try {
            BinaryContent content = findById(binaryId);
            binaryContentList.remove(content);
            fileRepository.save(binaryContentList);
            return true;
        } catch (BinaryContentNotFoundException e) {
            System.out.println("해당 바이너리 데이터는 존재하지 않습니다.");
            return false;
        }
    }
}
