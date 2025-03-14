package com.sprint.mission.discodeit.service.basic;

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
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    BinaryContent find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.find(id);
        return binaryContent;
    }

    List<BinaryContent> findAllByIdIn(UUID id) {
        List<BinaryContent> contentList = binaryContentRepository.findAllByIdIn(id);
        return contentList;
    }

    boolean delete(UUID id) {
        boolean delete = binaryContentRepository.delete(id);
        return delete;
    }
}
