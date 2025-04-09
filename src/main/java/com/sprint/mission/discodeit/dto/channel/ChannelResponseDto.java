package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResponseDto {

    private UUID id;
    private ChannelType type;
    private String name;
    private String description;
    private List<UUID> participantIds;
    private Instant lastMessageAt;

    public static ChannelResponseDto from(Channel channel,
        List<UUID> participantIds,
        Instant lastMessageAt) {
        
        ChannelResponseDto response = new ChannelResponseDto();
        response.setId(channel.getId());
        response.setName(channel.getName());
        response.setDescription(channel.getDescription());
        response.setType(channel.getType());
        response.setLastMessageAt(lastMessageAt);
        response.setParticipantIds(participantIds);

        return response;
    }
}
