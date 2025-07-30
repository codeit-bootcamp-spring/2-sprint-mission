package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.service.SseService;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class BasicSseService implements SseService {
    private static final Long TIMEOUT = 60L * 60 * 1000; // 1시간
    private final Map<String, Set<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    private final Map<String, SseEventCache> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(String userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        // emitter Set에 추가
        userEmitters
                .computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(emitter);

        // 연결 종료/에러 시 emitter 제거
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError((ex) -> removeEmitter(userId, emitter));

        // lastEventId가 있을 때 유실 이벤트 복구
        if (lastEventId != null) {
            resendMissedEvents(userId, lastEventId, emitter);
        }

        // 최초 연결 응답
        try {
            emitter.send(SseEmitter.event()
                    .id("ping")
                    .name("ping")
                    .data("connected"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // emitter 제거
    @Override
    public void removeEmitter(String userId, SseEmitter sseEmitter) {
        Set<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(sseEmitter);
        }
    }

    // 이벤트 전송 + 이벤트 캐싱
    public void sendEvent(String userId, String eventName, Object eventData) {
        String eventId = UUID.randomUUID().toString();

        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(eventId)
                .name(eventName)
                .data(eventData);

        eventCache.put(eventId, new SseEventCache(event, System.currentTimeMillis()));

        Set<SseEmitter> emitters = userEmitters.getOrDefault(userId, Collections.emptySet());
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(event);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }

    // 유실 이벤트 복구
    @Override
    public void resendMissedEvents(String userId, String lastEventId, SseEmitter sseEmitter) {
        // 마지막 ID 이후로 캐시된 이벤트를 찾아서 재전송
        eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try{
                        sseEmitter.send(entry.getValue().event);
                    } catch (IOException ex){
                        sseEmitter.completeWithError(ex);
                    }
                });
    }

    // 주기적으로 ping 스케줄링(응답 없는 emitter 정리)
    @Scheduled(fixedRate = 30000)
    public void sendPingToAll() {
        userEmitters.forEach((userId, emitters) -> {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("ping").data("ping"));
                } catch(IOException ex) {
                    emitter.complete();
                }
            });
        });

        // 오래된 캐시 정리
        long now = System.currentTimeMillis();
        eventCache.entrySet().removeIf(e -> now - e.getValue().timestamp > 10 * 60 * 1000);
    }

    // 내부 이벤트 캐시 구조
    @AllArgsConstructor
    static class SseEventCache {
        final SseEmitter.SseEventBuilder event;
        final long timestamp;
    }
}