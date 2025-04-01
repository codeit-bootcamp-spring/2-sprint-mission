package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBinaryContentRequest {
    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;
}
