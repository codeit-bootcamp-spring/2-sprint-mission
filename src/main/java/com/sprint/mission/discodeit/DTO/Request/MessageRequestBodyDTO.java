package com.sprint.mission.discodeit.DTO.Request;

import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.MessageWriteDTO;

import java.util.List;

public record MessageRequestBodyDTO(
        MessageWriteDTO messageWriteDTO,
        List<BinaryContentCreateDTO> binaryContentCreateDTO

) {
}
