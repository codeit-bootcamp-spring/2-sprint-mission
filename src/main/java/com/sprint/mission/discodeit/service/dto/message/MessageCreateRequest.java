package com.sprint.mission.discodeit.service.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class MessageCreateRequest {
    private String content;
    private final UUID channelId;
    private final UUID authorId;

    private BinaryContentType type;
    private List<MultipartFile> files;
}
