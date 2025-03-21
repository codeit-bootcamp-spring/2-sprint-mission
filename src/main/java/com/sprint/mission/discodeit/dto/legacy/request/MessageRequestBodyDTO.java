package com.sprint.mission.discodeit.dto.legacy.request;

import com.sprint.mission.discodeit.dto.requestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.requestToService.MessageWriteDTO;

import java.util.List;

public record MessageRequestBodyDTO(
        MessageWriteDTO messageWriteDTO,
        List<BinaryContentCreateDTO> binaryContentCreateDTO

) {
}
