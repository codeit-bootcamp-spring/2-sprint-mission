package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(CreateBinaryContentRequest request) {
        UUID userId = request.userId();
        UUID messageId = request.messageId();

        if (userId == null && messageId == null) {
            throw new IllegalArgumentException("userId 또는 messageId 중 하나는 반드시 있어야 합니다.");
        }
        if (userId != null && messageId != null) {
            throw new IllegalArgumentException("userId와 messageId는 동시에 설정될 수 없습니다.");
        }

        BinaryContent binaryContent = new BinaryContent(
                userId,
                messageId,
                request.fileName(),
                request.filePath()
        );

        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> find(UUID id) {
        return binaryContentRepository.getById(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.getAll().stream()
                .filter(file -> ids.contains(file.getId()))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
