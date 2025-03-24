package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BinaryContentCreateDto {
    private UUID referenceId;
    private String filePath;
}
