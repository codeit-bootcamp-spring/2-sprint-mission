package com.sprint.mission.discodeit.entityTest;

import com.sprint.mission.discodeit.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

class BaseEntityTest {

    private BaseEntity baseEntity;

    @BeforeEach
    void setUp() {
        baseEntity = new BaseEntity() {}; // 익명 클래스로 BaseEntity 생성
    }

    @Test
    @DisplayName("ID 자동 생성 확인")
    void testIdIsGenerated() {
        assertNotNull(baseEntity.getId(), "ID는 null이 아니어야 합니다.");
    }

    @Test
    @DisplayName("생성 시간 자동 저장 확인")
    void testCreatedAtIsSet() {
        assertNotNull(baseEntity.getCreatedAt(), "생성 시간이 null이 아니어야 합니다.");
        assertTrue(baseEntity.getCreatedAt() <= Instant.now().getEpochSecond(), "생성 시간이 현재 시간보다 클 수 없습니다.");
    }

    @Test
    @DisplayName("수정 시간 갱신 확인")
    void testUpdatedAtChanges() throws InterruptedException {
        Long initialUpdatedAt = baseEntity.getUpdatedAt();
        Thread.sleep(1000); // 1초 대기
        baseEntity.updateTimestamp();
        assertTrue(baseEntity.getUpdatedAt() > initialUpdatedAt, "업데이트 시간이 이전 시간보다 커야 합니다.");
    }
}