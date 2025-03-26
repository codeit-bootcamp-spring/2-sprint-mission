package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateBinaryContentDTO {
    private byte[] content;
    private String contentType;
    private UUID userId;
    private UUID messageId;
}
