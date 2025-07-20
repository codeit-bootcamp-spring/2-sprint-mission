package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;
    private static final Logger log = LoggerFactory.getLogger(BasicBinaryContentService.class);

    @Transactional
    @Override
    public BinaryContentDto create(MultipartFile file) {
        validateFile(file);
        BinaryContent binaryContent = saveMetadata(file);
        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    uploadFileAfterCommit(dto, file);
                } catch (IOException e) {
                    log.error("Exception thrown while initiating async file upload for {}", dto.id(), e);
                }
            }
        });

        return dto;
    }

    @Async("threadPoolTaskExecutor")
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000),
            value = {IOException.class, FileProcessingCustomException.class}
    )
    public void uploadFileAfterCommit(BinaryContentDto binaryContentDto, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        binaryContentStorage.put(binaryContentDto.id(), bytes).join();
        updateUploadStatus(binaryContentDto.id(), BinaryContentUploadStatus.SUCCESS);
    }

    @Recover
    public void recoverUploadFailure(Throwable ex, BinaryContentDto binaryContentDto, MultipartFile file) {
        updateUploadStatus(binaryContentDto.id(), BinaryContentUploadStatus.FAILED);
    }

    @Transactional
    public void updateUploadStatus(UUID contentId, BinaryContentUploadStatus status) {
        binaryContentRepository.findById(contentId).ifPresent(content -> {
            content.updateUploadStatus(status);
            binaryContentRepository.save(content);
        });
    }

    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(FileNotFoundException::new);
        return binaryContentMapper.toDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        if (CollectionUtils.isEmpty(binaryContentIds)) {
            return new ArrayList<>();
        }
        List<BinaryContent> contents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        return contents.stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        existFiles(binaryContentId);
        deleteException(binaryContentId);
        deleteMetadata(binaryContentId);
    }

    private void existFiles(UUID binaryContentId) {
        binaryContentRepository.findById(binaryContentId)
                .orElseThrow(FileNotFoundException::new);
    }

    private void deleteException(UUID binaryContentId) {
        try {
            binaryContentStorage.delete(binaryContentId);
            log.info("실제 파일 삭제 완료 - ID: {}", binaryContentId);
        } catch (FileNotFoundException e) {
            log.warn("실제 파일이 이미 없음 (삭제 계속) - ID: {}", binaryContentId);
        } catch (FileProcessingCustomException e) {
            log.error("파일 삭제 중 처리 오류 (메타데이터 삭제는 진행)");
        } catch (Exception e) {
            log.error("파일 삭제 중 예상치 못한 오류 - ID");
        }
    }

    private void deleteMetadata(UUID binaryContentId) {
        binaryContentRepository.deleteById(binaryContentId);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException();
        }
    }

    private BinaryContent saveMetadata(MultipartFile file) {
        BinaryContent meta = BinaryContent.of(
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType()
        );
        return binaryContentRepository.save(meta);
    }
}
