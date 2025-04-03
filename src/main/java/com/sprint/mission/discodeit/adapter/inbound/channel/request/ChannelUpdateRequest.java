package com.sprint.mission.discodeit.adapter.inbound.channel.request;

public record ChannelUpdateRequest(
    String newName,
    String newDescription
//    ChannelType newType
) {

}
