package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageRequest {

    private String content;
    private List<UUID> binaryContentIds;
}
