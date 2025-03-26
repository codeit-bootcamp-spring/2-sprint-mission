package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContentCreateRequestDTO {
    private UUID userId;
    private UUID messageId;
    private byte[] data;
}