package com.sprint.mission.discodeit.dto.message;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class MessageCreate {
    private String content;
    private UUID channelID;
    private UUID authorId;
    private List<UUID> contents;
}
