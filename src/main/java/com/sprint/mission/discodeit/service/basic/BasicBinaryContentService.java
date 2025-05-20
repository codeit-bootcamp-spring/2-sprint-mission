package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentStorageException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();

        log.info("▶▶ [SERVICE] Start creating binaryContent - fileName: {}, contentType: {}", fileName, contentType);
        log.debug("▶▶ [SERVICE] binaryContent byte size: {}", bytes.length);

        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(fileName)
                .size((long) bytes.length)
                .contentType(contentType)
                .build();

        binaryContent = binaryContentRepository.save(binaryContent);
        log.debug("▶▶ [SERVICE] Metadata saved for binaryContent - id: {}", binaryContent.getId());

        try {
            binaryContentStorage.put(binaryContent.getId(), bytes);
        } catch (Exception e) {
            log.error("◀◀ [SERVICE] Failed to save binaryContent to storage! fileName: {}", fileName, e);
            throw new BinaryContentStorageException(fileName);
        }

        log.info("◀◀ [SERVICE] Finished creating binaryContent - id: {}", binaryContent.getId());
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        log.info("▶▶ [SERVICE] Attempting to find binaryContent - id: {}", binaryContentId);

        return binaryContentRepository.findById(binaryContentId)
                .map(content -> {
                    log.debug("◀◀ [SERVICE] Successfully found binaryContent - fileName: {}, contentType: {}, size: {}",
                            content.getFileName(), content.getContentType(), content.getSize());
                    return content;
                })
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Failed to find binaryContent - id: {}", binaryContentId);
                    return new BinaryContentNotFoundException(binaryContentId);
                });
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        log.info("▶▶ [SERVICE] Attempting to find multiple binaryContents - ids: {}", binaryContentIds);

        List<BinaryContent> result = binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .toList();

        log.info("◀◀ [SERVICE] Finished finding multiple binaryContents - requested: {}, found: {}",
                binaryContentIds.size(), result.size());
        return result;
    }

    @Override
    public void delete(UUID binaryContentId) {
        log.info("▶▶ [SERVICE] Request to delete binaryContent - id: {}", binaryContentId);

        if (!binaryContentRepository.existsById(binaryContentId)) {
            log.warn("◀◀ [SERVICE] Failed to delete binaryContent (not found) - id: {}", binaryContentId);
            throw new BinaryContentNotFoundException(binaryContentId);
        }
        binaryContentRepository.deleteById(binaryContentId);
        log.info("◀◀ [SERVICE] Successfully deleted binaryContent - id: {}", binaryContentId);
    }

    @Override
    public ResponseEntity<?> download(UUID binaryContentId) {
        log.info("▶▶ [SERVICE] Start downloading binaryContent - id: {}", binaryContentId);

        BinaryContent binaryContent = find(binaryContentId);
        BinaryContentDto dto = new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
        log.info("◀◀ [SERVICE] Download response created for binaryContent - id: {}", binaryContentId);
        return binaryContentStorage.download(dto);
    }
}