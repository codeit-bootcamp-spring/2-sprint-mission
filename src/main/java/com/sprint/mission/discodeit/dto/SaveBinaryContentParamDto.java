package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.SubDirectory;

public record SaveBinaryContentParamDto(
        SubDirectory subDirectory,
        String filePath,
        byte[] profile
) {

}
