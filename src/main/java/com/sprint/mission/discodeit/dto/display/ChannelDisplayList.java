package com.sprint.mission.discodeit.dto.display;

import com.sprint.mission.discodeit.dto.ChannelFindDTO;

import java.util.List;

public record ChannelDisplayList(
        List<ChannelFindDTO> channels
) {
}
