package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void save(BinaryContentDTO binaryContentDTO) {
        BinaryContent binaryContent = new BinaryContent(binaryContentDTO.data());
        binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContentDTO findById(UUID id) {
        return binaryContentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found: " + id));
    }

    @Override
    public List<BinaryContentDTO> findAll() {
        return binaryContentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);
    }

    private BinaryContentDTO toDTO(BinaryContent binaryContent) {
        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getData(),
                binaryContent.getFileType()
        );
    }
}