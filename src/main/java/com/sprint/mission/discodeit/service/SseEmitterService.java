package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.storage.SseEventStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    // 사용자별 여러 emitter 관리
    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final SseEventStorage sseEventStorage;


    public SseEmitter connect(UUID userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 타임아웃

        // userId에 해당하는 List<SseEmitter>가 없으면 새로 만들고, 그 리스트에 emitter 추가
        emitters
            .computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>())
            .add(emitter);

        Runnable removeCallback = () -> removeEmitter(userId, emitter);
        emitter.onCompletion(removeCallback); // 정상 종료
        emitter.onTimeout(removeCallback);  // 타임아웃 종료
        emitter.onError(e -> removeCallback.run()); // 예외 발생 시 제거

        // Last-Event-ID 헤더를 보냈을 경우에만 복원 처리
        if (lastEventId != null && !lastEventId.isEmpty()) {
            List<Map.Entry<String, Object>> cachedEvents =
                sseEventStorage.findEventsAfter(userId, lastEventId);

            for (Map.Entry<String, Object> entry : cachedEvents) {
                try {
                    emitter.send(SseEmitter.event()
                        .id(entry.getKey())
                        .name("replay")
                        .data(entry.getValue()));
                } catch (Exception e) {
                    removeCallback.run(); // send 실패 시 emitter 제거
                }
            }
        }

        return emitter;
    }

    public List<SseEmitter> getEmitters(UUID userId) {
        return emitters.getOrDefault(userId, List.of());
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
        }
    }

    @Scheduled(fixedRate = 30_000)  // 30초마다 실행
    public void sendPing() {
        for (Map.Entry<UUID, List<SseEmitter>> entry : emitters.entrySet()) {
            UUID userId = entry.getKey();
            List<SseEmitter> userEmitters = entry.getValue();

            for (SseEmitter emitter : new ArrayList<>(userEmitters)) {
                try {
                    emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("keep-alive"));
                } catch (Exception e) {
                    removeEmitter(userId, emitter); // 예외 발생시 끊긴 emitter 제거
                }
            }
        }

    }
}
