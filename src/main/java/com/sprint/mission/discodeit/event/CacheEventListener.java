package com.sprint.mission.discodeit.event;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEventListener {

  private final CacheManager cacheManager;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCreateAllNotificationsEvent(GroupNotificationEvent event) {
    Set<UUID> receivers = event.receiverIds();
    log.debug("단체 알림 생성에 따른 해당 수신자들의 알림 캐시 삭제 시작 - receivers : {}", receivers);

    Cache cache = cacheManager.getCache("notifications");
    if (cache != null) {
      receivers.forEach(cache::evict);
      log.debug("알림 캐시 삭제 - receivers : {}", receivers);
    }
    log.debug("단체 알림 생성에 따른 해당 수신자들의 캐시 삭제 완료");
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCreatePrivateChannelEvent(NewPrivateChannelEvent event) {
    List<UUID> participantIds = event.participantIds();
    log.debug("비공개 채널 생성에 따른 참가자들의 채널 캐시 삭제 시작 - participantIds : {}", participantIds);

    Cache cache = cacheManager.getCache("channels");
    if (cache != null) {
      participantIds.forEach(cache::evict);
    }
    log.debug("비공개 채널 생성에 따른 참가자들의 채널 캐시 삭제 완료");
  }
}
