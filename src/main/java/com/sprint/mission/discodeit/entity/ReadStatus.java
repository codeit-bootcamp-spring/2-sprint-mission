package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

// 필드들의 값을 모두 받아와 생성자에서 할당시켜주는 어노테이션
@AllArgsConstructor
@Getter
@Setter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private UUID channelId;
    //
    private Instant createAt;
    private Instant lastRead;

    //
    public void update(Instant newLastReadAt){
        lastRead = newLastReadAt;
    }
}
