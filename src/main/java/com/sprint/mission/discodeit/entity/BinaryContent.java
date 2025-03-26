package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BinaryContent {
    private UUID id;
    private byte[] content;
    private String contentType;
    //
    private UUID userId;
    private UUID messageId;
}
