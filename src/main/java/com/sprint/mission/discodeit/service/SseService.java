package com.sprint.mission.discodeit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;
    private final Map<UUID, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Object>> userEventCache = new ConcurrentHashMap<>();
    private final int MAX_USER = 5;

    // 클라이언트 이벤트 구독-> 이벤트 보낼 수 있음
    public SseEmitter subscribe(UUID userId, String lastEventId) {
        List<SseEmitter> emitters = userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        // compute null 리스트 값
        userEmitters.compute(userId, (id, list) -> {
            if (list == null) list = new CopyOnWriteArrayList<>();
            if (list.size() >= MAX_USER) {
                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS, "최대 연결 수 초과");
            }
            list.add(emitter);
            return list;
        });
        // 콜백
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        });
        emitter.onError(e -> {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        });
        sendInitialEvent(emitter, userId);
        replayMissedEvents(emitter, userId, lastEventId);

        return emitter;
    }


    @Async
    public void notify(UUID userId, String eventName, Object eventData) {
        String eventId = generateEventId(userId);
        cacheEvent(userId, eventId, eventData);

        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.forEach(emitter -> sendEvent(emitter, eventId, eventName, eventData));
        }
    }

    // 브로드캐스트로 전송
    @Async
    public void broadcast(String eventName, Object eventData) {
        userEmitters.keySet().forEach(userId -> notify(userId, eventName, eventData));
    }

    @Scheduled(fixedRate = 20000)
    public void sendPing() {
        userEmitters.forEach((userId, emitters) -> {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().comment("ping"));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            });
        });
    }

    private void sendEvent(SseEmitter emitter, String eventId, String eventName, Object eventData) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(eventName)
                    .data(eventData));
        } catch (IOException e) {
            log.error("Failed to send SSE event to emitter: {}", emitter, e);
        }
    }

    private void sendInitialEvent(SseEmitter emitter, UUID userId) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected to notification service. User ID: " + userId));
        } catch (IOException e) {
            log.error("Failed to send initial SSE event to user: {}", userId, e);
        }
    }

    private void replayMissedEvents(SseEmitter emitter, UUID userId, String lastEventId) {
        if (lastEventId == null) {
            return;
        }
        Map<String, Object> events = userEventCache.get(userId);
        if (events == null) {
            return;
        }
        events.entrySet().stream()
                .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                .forEach(entry -> sendEvent(emitter, entry.getKey(), "message", entry.getValue()));
    }

    private String generateEventId(UUID userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void cacheEvent(UUID userId, String eventId, Object eventData) {
        Map<String, Object> events = userEventCache.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        events.put(eventId, eventData);
    }
} 