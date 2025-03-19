package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.BinaryContent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
    public BinaryContentDTO create(CreateBinaryContentParam createBinaryContentParam) {
        BinaryContent binaryContent = createBinaryContentEntity(createBinaryContentParam);
        binaryContentRepository.save(binaryContent);
        return binaryContentEntityToDTO(binaryContent);
    }

    @Override
    public BinaryContentDTO find(UUID id) {
        BinaryContent binaryContent = findBinaryContentById(id);
        return binaryContentEntityToDTO(binaryContent);
    }

    @Override
    // attachmentIds를 받아서 리스트를 조회
    public List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentsId) {
        return  attachmentsId.stream()
                .map(id -> findBinaryContentById(id)) // attachmentsId로 BinaryContent 찾아서
                .map(bc -> binaryContentEntityToDTO(bc)) // DTO로 변환
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    private BinaryContentDTO binaryContentEntityToDTO(BinaryContent binaryContent) {
        return BinaryContentDTO.builder()
                .content(binaryContent.getContent())
                .filename(binaryContent.getFilename())
                .createdAt(binaryContent.getCreatedAt())
                .id(binaryContent.getId())
                .build();
    }

    private BinaryContent createBinaryContentEntity(CreateBinaryContentParam createBinaryContentParam) {
        return BinaryContent.builder()
                .content(createBinaryContentParam.content())
                .filename(createBinaryContentParam.filename())
                .build();
    }

    private BinaryContent findBinaryContentById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " binaryContent가 존재하지 않습니다."));
    }
}
