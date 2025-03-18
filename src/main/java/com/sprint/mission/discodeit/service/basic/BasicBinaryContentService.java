package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.legacy.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @CustomLogging
    @Override
    public BinaryContent create(BinaryContentDTO binaryContentDTO) {
        int size = binaryContentDTO.bytes().length;
        BinaryContent binaryContent = new BinaryContent(binaryContentDTO.fileName(), size, binaryContentDTO.contentType(),binaryContentDTO.bytes());
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID binaryId) {
        BinaryContent binaryContent = binaryContentRepository.find(binaryId);
        return binaryContent;
    }
    @Override
    public List<BinaryContent> findAllByIdIn() {
        List<BinaryContent> contentList = binaryContentRepository.findAllByIdIn();
        return contentList;
    }

    @CustomLogging
    @Override
    public boolean delete(UUID binaryId) {
        boolean delete = binaryContentRepository.delete(binaryId);
        return delete;
    }
}
