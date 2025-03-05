package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BaseEntity {
    protected UUID id;         // UUID 타입 고유 ID
    protected Long createdAt;  // 생성 시간 (유닉스 타임 스탬프)
    protected Long updatedAt;  // 수정 시간

    public BaseEntity() {
        this.id = UUID.randomUUID(); // 랜덤 UUID 자동 생성
        this.createdAt = System.currentTimeMillis(); // 현재 시간 으로 초기화 -> 현재 시간 저장
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    //추상 메소드 - 구현체 클래스 가 구체적 으로 구현 해야함. (추상 메소드 는 모든 자식 클래스 에 정의 해야 함)
}
