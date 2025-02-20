package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channelName;

    public Channel(String channelName) {
        super();  // BaseEntity 클래스의 생성자 호출 -> id, createdAt 초기화
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    // 업데이트 메서드 (채널명 변경)
    public void update(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();  // 수정 시간 업데이트
    }
}
