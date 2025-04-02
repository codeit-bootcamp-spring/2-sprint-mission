package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest requestDto) {
        BinaryContent binaryContent = new BinaryContent(
                requestDto.fileName(),
                (long)requestDto.bytes().length,
                requestDto.contentType(),
                requestDto.bytes());
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 BinaryContent 없음"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new ResourceNotFoundException("해당 BinaryContent 없음");
        }
        binaryContentRepository.deleteById(id);
    }
}
