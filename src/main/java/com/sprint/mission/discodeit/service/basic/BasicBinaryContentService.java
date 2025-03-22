package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.create.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.logging.CustomLogging;
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

    @CustomLogging
    @Override
    public BinaryContent create(CreateBinaryContentRequestDTO createBinaryContentRequestDTO) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentRequestDTO.fileName(),(long) createBinaryContentRequestDTO.bytes().length, createBinaryContentRequestDTO.contentType(),createBinaryContentRequestDTO.bytes());
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryId);
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
