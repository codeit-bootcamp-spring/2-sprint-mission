package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage; // TODO: 5/7/25 서비스로 변경

    @Transactional
    @Override
    public BinaryContentResult create(BinaryContentRequest binaryContentRequest) {
        if (binaryContentRequest == null) {
            return null;
        }

        BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType());

        binaryContentStorage.put(binaryContent.getId(), binaryContentRequest.bytes());
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        return BinaryContentResult.fromEntity(savedBinaryContent);
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
        return ids.stream()
                .map(this::getById)
                .toList();
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
