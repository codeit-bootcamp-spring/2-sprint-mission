package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        log.info("File 생성 시작");
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();
        BinaryContent binaryContent = new BinaryContent(
            fileName,
            (long) bytes.length,
            contentType
        );
        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), bytes);
        log.info("File 생성 완료");
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
            .map(binaryContentMapper::toDto)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                Map.of("BinaryContent is not exist", binaryContentId)));
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds).stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public void delete(UUID binaryContentId) {
        log.info("File 삭제 시작");
        if (!binaryContentRepository.existsById(binaryContentId)) {
            log.warn("File이 존재하지 않습니다.");
            throw new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                Map.of("BinaryContent is not exist", binaryContentId));
        }
        binaryContentRepository.deleteById(binaryContentId);
        log.info("File 삭제 완료");
    }
}
