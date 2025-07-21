package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.FileUploadEvent;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    @EventListener
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Transactional
    public void handleFileUploadEvent(FileUploadEvent event) {
        binaryContentStorage.put(event.binaryContentId(), event.fileBytes());

        BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
                .orElseThrow(FileNotFoundException::new);
        binaryContent.completeUpload();
    }

    @Recover
    @Transactional
    public void recover(RuntimeException e, FileUploadEvent event) {
        BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
                .orElse(null);

        if (binaryContent != null) {
            binaryContent.failUpload();
        } else {
            log.error("복구 로직 실행 중 BinaryContent를 찾을 수 없음: {}", event.binaryContentId());
        }
    }
}
