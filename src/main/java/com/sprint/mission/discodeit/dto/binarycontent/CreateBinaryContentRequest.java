package com.sprint.mission.discodeit.dto.binarycontent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBinaryContentRequest {

    private String name;
    private String contentType;
    private byte[] bytes;
}
