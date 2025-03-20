package com.sprint.mission.discodeit.service.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateParam(
        BinaryContentType type,
        List<MultipartFile> files
) {
}
