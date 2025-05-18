package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentCore {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
        if (binaryContentRequest == null) {
            log.warn("바이너리 컨텐츠 생성 요청이 null입니다.");
            return null;
        }
        log.info("바이너리 컨텐츠 저장 요청: fileName={}, size={}", binaryContentRequest.fileName(), binaryContentRequest.size());
        BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType(), binaryContentRequest.size()));
        binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
        log.info("바이너리 컨텐츠 저장 성공: binaryContentId={}", savedBinaryContent.getId());

        return savedBinaryContent;
    }

    @Transactional
    public List<BinaryContent> createBinaryContents(List<BinaryContentRequest> binaryContentRequests) {
        if (binaryContentRequests == null) {
            log.warn("바이너리 컨텐츠 생성 요청이 null입니다.");
            return null;
        }

        log.info("여러 바이너리 컨텐츠 저장 요청: 요청 수={}", binaryContentRequests.size());
        List<BinaryContent> binaryContents = new ArrayList<>();
        for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
            BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType(), binaryContentRequest.size()));
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
            binaryContents.add(savedBinaryContent);
        }
        log.info("여러 바이너리 컨텐츠 저장 완료: 저장된 수={}", binaryContents.size());

        return binaryContents;
    }

    public void delete(UUID id) {
        log.warn("바이너리 컨텐츠 삭제 요청: binaryContentId={}", id);
        if (!binaryContentRepository.existsById(id)) {
            throw new BinaryContentNotFoundException(Map.of("userId", id));
        }

        binaryContentRepository.deleteById(id);
        log.info("바이너리 컨텐츠 삭제 성공: binaryContentId={}", id);
    }

}
