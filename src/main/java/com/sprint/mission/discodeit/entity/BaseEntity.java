package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

// parent class (abstract)
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    // 각 객체를 식별하는 UUID
    // user, message, channel 모두 각 객체 별 할당
    protected UUID id;
    // 객체가 생성된 unix time stamp
    protected long createdAt;
    // 마지막으로 수정된 시간
    protected long updatedAt;

    // 생성자
    public BaseEntity(){
        // 아직 누가 사용하는지 모르니까 random으로 할당
        this.id = UUID.randomUUID();
        // 새로 만들어진 시간과 update시간 모두 현재 시간으로 등록
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void updateTimestamp() {
        // update된 시간 기록
        this.updatedAt = System.currentTimeMillis();
    }
}
