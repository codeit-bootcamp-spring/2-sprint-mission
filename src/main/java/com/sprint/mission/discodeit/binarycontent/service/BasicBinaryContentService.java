package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentCore binaryContentCore;
    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    @Override
    public BinaryContentResult createBinaryContent(BinaryContentRequest binaryContentRequest) {
        log.info("파일 메타데이터 생성 요청: fileName={}, size={}", binaryContentRequest.fileName(), binaryContentRequest.bytes() != null ? binaryContentRequest.bytes().length : 0);
        BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);
        log.info("파일 메타데이터 생성 성공: binaryContentId={}", binaryContent.getId());

        return BinaryContentResult.fromEntity(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentResult getById(UUID id) {
        log.debug("파일 메타데이터 단건 조회 요청: binaryContentId={}", id);
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID를 가진 컨텐츠가 없습니다."));
        log.info("파일 메타데이터 단건 조회 성공: binaryContentId={}", id);

        return BinaryContentResult.fromEntity(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentResult> getByIdIn(List<UUID> ids) {
        log.debug("여러 파일 메타데이터 조회 요청: binaryContentIds={}", ids);
        List<BinaryContentResult> binaryContentResults = binaryContentRepository.findAllById(ids)
                .stream()
                .map(BinaryContentResult::fromEntity)
                .toList();
        log.info("여러 파일 메타데이터 조회 성공: 조회 수={}", binaryContentResults.size());

        return binaryContentResults;
    }

    @Override
    public void delete(UUID id) {
        log.warn("파일 메타데이터 삭제 요청: binaryContentId={}", id);
        binaryContentCore.delete(id);
        log.info("파일 메타데이터 삭제 성공: binaryContentId={}", id);
    }

}
