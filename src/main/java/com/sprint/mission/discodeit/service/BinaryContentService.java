package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    BinaryContentRepository binaryContentRepository;

    public void createBinaryContent(BinaryContentCreateDto dto) {
        binaryContentRepository.addBinaryContent(new BinaryContent(dto.getReferenceId(), dto.getFilePath()));
    }

    public BinaryContent findBinaryContent(UUID binaryContentId) {
        return binaryContentRepository.findBinaryContentById(binaryContentId);
    }

    public List<BinaryContent> findAllBinaryContent() {
        return binaryContentRepository.findAllBinaryContents();
    }

    public void deleteBinaryContent(UUID binaryContentId) {
        binaryContentRepository.deleteBinaryContent(binaryContentId);
    }
}
