package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.create.BinaryContentCreateRequestDTO;
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
    public BinaryContent create(BinaryContentCreateRequestDTO binaryContentCreateRequestDTO) {
        BinaryContent binaryContent = new BinaryContent(binaryContentCreateRequestDTO.fileName(), binaryContentCreateRequestDTO.bytes().length, binaryContentCreateRequestDTO.contentType(), binaryContentCreateRequestDTO.bytes());
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        return binaryContentRepository.findById(binaryId);
    }
    @Override
    public List<BinaryContent> findAllByIdIn() {
        return binaryContentRepository.findAllByIdIn();
    }

    @CustomLogging
    @Override
    public void delete(UUID binaryId) {
        binaryContentRepository.delete(binaryId);
    }
}
