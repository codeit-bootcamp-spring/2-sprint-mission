package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/*
@Builder를 사용하면 자동으로 빌더 패턴이 적용된 생성자가 생성됨! // 아예 생성자를 작성할 필요가 없음.
// 기존 방식은 생성자 호출 생성, 빌더 패턴은 객체 직접 생, 높은 가독성과 많은 매개변수로 인한 실수 가능성이 낮아짐.
@Getter 추가해서 getId(), getName() 등 Getter 메서드 자동 생성
id, createdAt은 final로 선언해서 불변 객체 유지
 */

@Builder
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;

    private Long updatedAt;
    private ChannelType type;
    private String name;
    private String description;

//    public Channel(ChannelType type, String name, String description) {
//        this.id = UUID.randomUUID();
//        this.createdAt = Instant.now().getEpochSecond();
//        //
//        this.type = type;
//        this.name = name;
//        this.description = description;
//    }

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

    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
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
