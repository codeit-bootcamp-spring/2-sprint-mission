package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UUID create(CreateBinaryContentRequest request) {
        String fileName = request.getFileName();
        String contentType = request.getContentType();
        byte[] bytes = request.getBytes();

        BinaryContent binaryContent =
            new BinaryContent(
                fileName,
                bytes.length,
                contentType,
                bytes
            );

        return binaryContent.getId();
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.find(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream()
            .filter(bc -> binaryContentIds.contains(bc.getId())).toList();
    }

    @Override
    public void delete(UUID contentId) {
        binaryContentRepository.delete(contentId);
    }
}
