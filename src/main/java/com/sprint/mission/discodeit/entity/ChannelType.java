package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    PUBLIC,  // 공개 채널 (모든 유저가 참여 가능)
    PRIVATE; // 비공개 채널 (초대된 유저만 참여 가능)

    // 선택적으로 설명 추가 가능
    public boolean isPublic() {
        return this == PUBLIC;
    }

    public boolean isPrivate() {
        return this == PRIVATE;
    }
}
