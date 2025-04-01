package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
// 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
// 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
public class ReadStatus extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super(); // BaseEntity 생성자 호출
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateReadStatus(Instant readAt) {
        this.lastReadAt = readAt;
    }
}