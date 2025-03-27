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
    BinaryContentRepository binaryContentRepository;

    public void createBinaryContent(CreateBinaryContentRequest request) {
        binaryContentRepository.addBinaryContent(new BinaryContent(request.getReferenceId(), request.getFilePath()));
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
