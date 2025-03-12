package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 📌 "마커 인터페이스"란?
 * 보통 인터페이스는 메서드를 정의하고, 이를 구현하는 클래스에서 메서드를 오버라이드해야 해.
 * 하지만 Serializable은 아무 메서드도 없음! java에서 제공하는 기본 인터페이스. (구현 필요x)
 * 왜냐하면 JVM(자바 가상 머신)이 이 인터페이스가 구현되었는지만 확인하고, 직렬화 가능 여부를 판단하기 때문이야.
 * 즉, "이 객체는 직렬화할 수 있어!" 라는 표시(마커) 역할만 함.
 */

public class Channel implements Serializable {
    // 직렬화 버전 관리, 클래스의 변동에 영향을 받지 않게 버전 명시?
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    //
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = Instant.now().getEpochSecond();
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public ChannelType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void updateName(String newName) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }

    public void updateDesc(String newDescription) {
        boolean anyValueUpdated = false;
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
