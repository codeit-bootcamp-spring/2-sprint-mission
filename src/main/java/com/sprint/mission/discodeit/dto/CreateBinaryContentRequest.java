package com.sprint.mission.discodeit.dto;

import lombok.Data;

@Data
public class CreateBinaryContentRequest {
    private String fileName;
    private String contentType;
    private byte[] bytes;
}
