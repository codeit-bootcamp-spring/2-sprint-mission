package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateBinaryContentRequest;
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

    public UUID createBinaryContent(CreateBinaryContentRequest request) {
        BinaryContent binaryContent = new BinaryContent(request.getFileName(), request.getSize(), request.getContentType(), request.getBytes());
        binaryContentRepository.addBinaryContent(binaryContent);

        return binaryContent.getId();
    }

    public BinaryContent findBinaryContent(UUID binaryContentId) {
        return binaryContentRepository.findBinaryContentById(binaryContentId);
    }

    public List<BinaryContent> findAllBinaryContent() {
        return binaryContentRepository.findAllBinaryContents();
    }

    public void deleteBinaryContent(UUID binaryContentId) {
        binaryContentRepository.deleteBinaryContentById(binaryContentId);
    }
}
