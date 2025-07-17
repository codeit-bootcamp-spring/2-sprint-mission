package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.async.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.dto.event.FileUploadEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadEventHandler {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async("fileUploadExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(
        FileUploadEvent event
    ) {
        binaryContentStorage.put(event.id(), event.bytes())
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("파일 업로드 실패: id={}, message={}", event.id(), throwable.getMessage());
                    binaryContentService.updateStatus(event.id(), BinaryContentUploadStatus.FAILED);
                } else {
                    log.info("파일 업로드 성공: id={}", result);
                    binaryContentService.updateStatus(event.id(), BinaryContentUploadStatus.SUCCESS);
                }
            });
    }
}
