package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.RestExceptions;
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

    // BinaryContentService는 Controller에서 호출되는 것이 아닌, MessageService나 UserService에서 호출되는 구조
    // => 서비스간 호출이므로 엔티티를 받거나 반환해도 상관없다
    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        binaryContentRepository.save(binaryContent);
        return binaryContent;
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
        return new BinaryContentDTO(binaryContent.getId(), binaryContent.getCreatedAt(), binaryContent.getFilename(), binaryContent.getSize(), binaryContent.getContentType(), binaryContent.getBytes());
    }

    private BinaryContent createBinaryContentEntity(CreateBinaryContentParam createBinaryContentParam) {
        return BinaryContent.builder()
                .filename(createBinaryContentParam.filename())
                .size(createBinaryContentParam.size())
                .contentType(createBinaryContentParam.contentType())
                .bytes(createBinaryContentParam.bytes())
                .build();
    }

    private BinaryContent findBinaryContentById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> RestExceptions.BINARY_CONTENT_NOT_FOUND);
    }
}
