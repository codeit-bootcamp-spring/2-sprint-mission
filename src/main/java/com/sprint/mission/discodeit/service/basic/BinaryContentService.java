package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    BinaryContent create() {
        BinaryContent binaryContent = new BinaryContent();
        BinaryContentDTO binaryContentDTO = BinaryContentDTO.create(binaryContent);
        binaryContentRepository.save(binaryContentDTO);
        return binaryContent;
    }

    BinaryContent find(UUID binaryId) {
        BinaryContent binaryContent = binaryContentRepository.find(binaryId);
        return binaryContent;
    }

    List<BinaryContent> findAllByIdIn() {
        List<BinaryContent> contentList = binaryContentRepository.findAllByIdIn();
        return contentList;
    }

    boolean delete(UUID binaryId) {
        boolean delete = binaryContentRepository.delete(binaryId);
        return delete;
    }
}
