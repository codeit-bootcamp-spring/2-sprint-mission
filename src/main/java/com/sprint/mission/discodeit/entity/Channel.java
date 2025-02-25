package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 업데이트 메서드 (채널명 변경)
    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();  // 수정 시간 업데이트
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
