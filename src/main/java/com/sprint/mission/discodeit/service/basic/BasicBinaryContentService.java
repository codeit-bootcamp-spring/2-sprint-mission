package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(
                request.fileName(),
                request.type(),
                request.bytes().length,
                request.bytes()
        );
        return binaryContentRepository.save(binaryContent);
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
            throw new IllegalArgumentException(id + " 에 해당하는 BinaryContent를 찾을 수 없음");
        }
        binaryContentRepository.deleteById(id);
    }
}
