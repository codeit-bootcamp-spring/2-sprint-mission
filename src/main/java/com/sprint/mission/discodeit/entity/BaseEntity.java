package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class BaseEntity {

    // 필드 선언
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    // Common 생성자 선언
    public BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis() / 1000;
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
        return convertToFormattedDate(createdAt);
    }

    public String getupdatedAttFormatted() {
        return convertToFormattedDate(updatedAt);
    }


    // 업데이트 메소드
    public void update(){
        this.updatedAt = System.currentTimeMillis() / 1000; // 수정 시에 현재 시간으로 업데이트
    }


    // 시간을 변환하는 메소드
    private String convertToFormattedDate(Long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp); // Long을 Instant로 변환
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")  // 원하는 형식으로 지정
                .withZone(ZoneId.of("Asia/Seoul")); // 한국 기준으로 출력
        return formatter.format(instant);
    }

}
