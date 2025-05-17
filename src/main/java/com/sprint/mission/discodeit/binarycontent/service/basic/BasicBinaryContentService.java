package com.sprint.mission.discodeit.binarycontent.service.basic;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentCore binaryContentCore;
    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    @Override
    public BinaryContentResult createBinaryContent(BinaryContentRequest binaryContentRequest) {
        return BinaryContentResult.fromEntity(binaryContentCore.createBinaryContent(binaryContentRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentResult getById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID를 가진 컨텐츠가 없습니다."));

        return BinaryContentResult.fromEntity(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentResult> getByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids)
                .stream()
                .map(BinaryContentResult::fromEntity)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentCore.delete(id);
    }

}
