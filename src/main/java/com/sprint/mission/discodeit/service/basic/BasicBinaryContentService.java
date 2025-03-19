package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BasicBinaryContentService(
            @Qualifier("fileBinaryContentRepository") BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public List<UUID> create(BinaryContentCreateParam createParam) {
        Path FilePath = Paths.get(System.getProperty("user.dir"), "file-data-map",
                BinaryContent.class.getSimpleName());

        return createParam.files().stream()
                .map(file -> {
                    BinaryContent bc = new BinaryContent(createParam.type(), file.getOriginalFilename(),
                            FilePath, file.getSize());
                    BinaryContent resBinaryContent = binaryContentRepository.save(bc, file);
                    return resBinaryContent.getId();
                }).toList();
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return idList.stream().map(this::findById).toList();
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음");
        }
        binaryContentRepository.deleteById(id);
    }
}
