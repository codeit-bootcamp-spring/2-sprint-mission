package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryDto.BinaryContentCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent createBinaryContent(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(request.contentId(), request.data());
        return binaryContentRepository.save(binaryContent);
    }

    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BinaryContent not found"));
    }

    public List<BinaryContent> findAllById(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    public void deleteById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
