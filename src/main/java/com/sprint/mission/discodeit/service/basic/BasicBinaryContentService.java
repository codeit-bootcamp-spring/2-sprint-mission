package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        return binaryContentMapper.toBinaryContentDTO(binaryContent);
    }

    @Override
    // attachmentIds를 받아서 리스트를 조회
    public List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentsId) {
        return  attachmentsId.stream()
                .map(id -> findBinaryContentById(id)) // attachmentsId로 BinaryContent 찾아서
                .map(bc -> binaryContentMapper.toBinaryContentDTO(bc)) // DTO로 변환
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    private BinaryContent findBinaryContentById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("binaryContent 찾기 실패: {}", id);
                    return RestExceptions.BINARY_CONTENT_NOT_FOUND;
                });
    }
}
