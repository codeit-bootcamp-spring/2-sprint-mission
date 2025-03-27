package com.sprint.mission.discodeit.dto.controller.channel;

import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;

import java.util.List;

public record ChannelListResponseDTO(
        List<FindChannelDTO> findChannelDTOList
) {
}
