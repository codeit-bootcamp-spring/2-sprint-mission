package com.sprint.mission.discodeit.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SseEventStorage {

    private final Map<UUID, LinkedHashMap<String, Object>> storage = new ConcurrentHashMap<>();
    private static final int MAX_EVENTS = 100;

    public void saveEvent(UUID userId, String eventId, Object data) {
        storage.computeIfAbsent(userId, id -> new LinkedHashMap<>());

        LinkedHashMap<String, Object> events = storage.get(userId);
        events.put(eventId, data);

        // 이벤트 개수 초과시 오래된 것 부터 제거
        if (events.size() > MAX_EVENTS) {
            Iterator<String> iterator = events.keySet().iterator();
            iterator.next();
            iterator.remove();
        }
    }

    // 마지막으로 받은 eventId 이후의 이벤트만 반환
    public List<Map.Entry<String, Object>> findEventsAfter(UUID userId, String lastEventId) {
        LinkedHashMap<String, Object> events = storage.get(userId);
        if (events == null) {
            return List.of();
        }

        boolean found = false;
        List<Map.Entry<String, Object>> result = new ArrayList<>();

        for (Map.Entry<String, Object> entry : events.entrySet()) {
            if (found) {
                result.add(entry);  // lastEventId를 찾은 이후부터는 결과에 추가
            }
            if (entry.getKey().equals(lastEventId)) {
                found = true;
            }
        }

        return result;
    }
}
