package com.sprint.mission.discodeit.adapter.inbound.server.dto;

import java.util.List;

public record ServerDisplayList(
        List<ServerDisplayItem> servers
) {
}
