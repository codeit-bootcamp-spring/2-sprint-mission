package com.sprint.mission.discodeit.domain.binarycontent.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentCore {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional(propagation = Propagation.REQUIRES_NEW) // 이렇게 하면 상관이 없나?
//  @Transactional // 이렇게 하면 상관이 없나?
  public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    log.debug("바이너리 스토리지 저장"); // 아 이건 시점 문제가 없긴하지
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

//    TransactionSynchronizationManager.registerSynchronization(
//        new TransactionSynchronization() {
//          @Override
//          public void afterCommit() {
//            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
//          }
//        }
//    );

    log.debug("바이너리 스토리지 이동전");
    binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

    return savedBinaryContent;
  }

  @Transactional
  public List<BinaryContent> createBinaryContents(
      List<BinaryContentRequest> binaryContentRequests
  ) {
    if (binaryContentRequests == null || binaryContentRequests.isEmpty()) {
      return null;
    }
    // 로직 수정 필요
    List<BinaryContent> binaryContents = new ArrayList<>();
    for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
      BinaryContent binaryContent = new BinaryContent(
          binaryContentRequest.fileName(),
          binaryContentRequest.contentType(),
          binaryContentRequest.size());

      BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronization() {
            @Override
            public void afterCommit() {
              log.debug("처음 바이너리 컨텐츠처리 쓰레드 {} ", Thread.currentThread().getName());
              binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
            }
          }
      );

      binaryContents.add(savedBinaryContent);
    }

    return binaryContents;
  }

  public void delete(UUID id) {
    validateBinaryContentExist(id);

    binaryContentRepository.deleteById(id);
  }

  private void validateBinaryContentExist(UUID id) {
    if (binaryContentRepository.existsById(id)) {
      return;
    }
    throw new BinaryContentNotFoundException(Map.of());
  }

}
