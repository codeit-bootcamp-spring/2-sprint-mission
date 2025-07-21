package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import java.util.*;
import lombok.Data;
import lombok.Getter;


public record BinaryContentDto
    (UUID id,
     String fileName,
     Long size,
     String contentType,
     BinaryContentUploadStatus uploadStatus
    ) {

}
