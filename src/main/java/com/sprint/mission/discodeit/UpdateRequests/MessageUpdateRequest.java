package com.sprint.mission.discodeit.UpdateRequests;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

@Getter
public class MessageUpdateRequest {
    private String message;
    private String userId;
    private String channelId;


}
