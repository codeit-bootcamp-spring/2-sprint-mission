package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;


public record BinaryContentListResponse(
    List<BinaryContent> data
) {

}
