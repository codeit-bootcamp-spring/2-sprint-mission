package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResult createProfileImage(BinaryContentRequest binaryContentRequest) {
        if (binaryContentRequest == null) {
            return null;
        }

        BinaryContent binaryContent = new BinaryContent(
                binaryContentRequest.fileName(),
                binaryContentRequest.contentType(),
                binaryContentRequest.bytes());

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        return BinaryContentResult.fromEntity(savedBinaryContent);
    }

    @Override
    public BinaryContentResult getById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findByBinaryContentId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 컨텐츠가 없습니다."));

        return BinaryContentResult.fromEntity(binaryContent);
    }

    @Override
    public List<BinaryContentResult> getByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::getById)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);
    }
}
