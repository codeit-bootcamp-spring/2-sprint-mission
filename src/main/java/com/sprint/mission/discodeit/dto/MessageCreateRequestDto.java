package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
public class MessageCreateRequestDto {
    private String content;
    private UUID channelId;
    private UUID authorId;
    private List<byte[]> attachmentDataList;

    public MessageCreateRequestDto(String content, UUID channelId, UUID authorId, List<byte[]> attachmentDataList) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentDataList = attachmentDataList;
    }
}
