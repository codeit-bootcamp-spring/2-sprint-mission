package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;

    public User(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 업데이트 메서드
    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();  // 수정 시간 업데이트
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
