package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicBinaryContentService {
    private BinaryContentRepository binaryContentRepository;

    public List<UUID> create(BinaryContentCreateParam createParam) {
        return createParam.files().stream()
                .map(file -> {
                    BinaryContent bc = new BinaryContent(createParam.type(), file.getOriginalFilename(),
                            file.getSize());
                    BinaryContent resBinaryContent = binaryContentRepository.save(bc, file);
                    return resBinaryContent.getId();
                }).toList();
    }

    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return idList.stream().map(this::findById).toList();
    }

    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음");
        }
        binaryContentRepository.deleteById(id);
    }
}
