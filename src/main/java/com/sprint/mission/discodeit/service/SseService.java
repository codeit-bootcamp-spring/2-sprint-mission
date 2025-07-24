package com.sprint.mission.discodeit.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter connect(String userId, String lastEventId);

    void removeEmitter(String userId,SseEmitter sseEmitter);

    void resendMissedEvents(String userId, String lastEventId, SseEmitter sseEmitter);

    void sendEvent(String userId, String eventName, Object eventData);
}