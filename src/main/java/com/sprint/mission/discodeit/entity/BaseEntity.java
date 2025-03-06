package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 필드 선언
    protected final UUID id;
    protected final Long createdAt;
    protected Long updatedAt;

    // Common 생성자 선언
    public BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt; // 첫 업데이트 시간은 생성시간과 동일
    }

    // getter 생성
    public UUID getId(){
        return id;
    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public Long getUpdatedAt(){
        return updatedAt;
    }

    public String getCreatedAtFormatted() {
        return Util.convertToFormattedDate(createdAt);
    }

    public String getUpdatedAttFormatted() {
        return Util.convertToFormattedDate(updatedAt);
    }

    // 업데이트 메소드
    public void update(){
        this.updatedAt = Instant.now().getEpochSecond(); // 수정 시에 현재 시간으로 업데이트
    }

    // 시간 변환
    private static class Util {
        private static String convertToFormattedDate(Long timestamp) {
            Instant instant = Instant.ofEpochSecond(timestamp); // Long을 Instant로 변환
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")  // 원하는 형식으로 지정
                    .withZone(ZoneId.of("Asia/Seoul")); // 한국 기준으로 출력
            return formatter.format(instant);
        }
    }
}
