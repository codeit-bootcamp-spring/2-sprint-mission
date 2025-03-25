package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateBinaryContentDto {
    private UUID referenceId;
    private String filePath;
}
