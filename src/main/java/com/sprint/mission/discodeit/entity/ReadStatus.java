package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

// 필드들의 값을 모두 받아와 생성자에서 할당시켜주는 어노테이션
@AllArgsConstructor
@Getter
public class ReadStatus {
    private UUID id;
    private UUID userid;
    private UUID channelId;
    private Instant lastRead;
}
