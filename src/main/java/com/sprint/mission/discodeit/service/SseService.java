package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class SseService {

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(String userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitters.computeIfAbsent(userId, key -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));

        sendPing(emitter);
        return emitter;
    }

    private void removeEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(userId);
        if (list != null) {
            list.remove(emitter);
        }
    }

    @Scheduled(fixedRate = 30_000)
    public void sendPingToAll() {
        for (var entry : emitters.entrySet()) {
            for (var emitter : entry.getValue()) {
                sendPing(emitter);
            }
        }
    }

    private void sendPing(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("ping").data("ping"));
        } catch (IOException e) {
            // 무시
        }
    }

    private void sendEvent(String userId, String eventName, Object data) {
        List<SseEmitter> list = emitters.get(userId);
        if (list == null) {
            return;
        }

        String eventId = UUID.randomUUID().toString();
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(eventName)
                    .data(data));
            } catch (IOException e) {
                removeEmitter(userId, emitter);
            }
        }
    }

    public void notifyNewNotification(String userId, NotificationDto notificationDto) {
        sendEvent(userId, "notifications", notificationDto);
    }

    public void notifyBinaryContentStatus(String binaryContentId,
        BinaryContentDto binaryContentDto) {
        sendEvent(binaryContentId, "binaryContents.status", binaryContentDto);
    }

    public void notifyChannelRefresh(String eventId, String channelId) {
        sendEvent(eventId, "channels.refresh", Map.of("channelId", channelId));
    }

    public void notifyUserRefresh(String eventId, String refreshedUserId) {
        sendEvent(eventId, "users.refresh", Map.of("userId", refreshedUserId));
    }

}
