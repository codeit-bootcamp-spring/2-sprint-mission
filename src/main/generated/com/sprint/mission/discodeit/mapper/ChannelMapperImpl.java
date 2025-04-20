package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-20T19:51:53+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (JetBrains s.r.o.)"
)
@Component
public class ChannelMapperImpl extends ChannelMapper {

    @Override
    public ChannelDto toDto(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = channel.getId();
        type = channel.getType();
        name = channel.getName();
        description = channel.getDescription();

        Instant lastMessageAt = getLastMessageTime(channel);
        Set<UserDto> participants = mapParticipants(channel);

        ChannelDto channelDto = new ChannelDto( id, type, name, description, participants, lastMessageAt );

        return channelDto;
    }

    @Override
    public List<ChannelDto> toDto(List<Channel> channels) {
        if ( channels == null ) {
            return null;
        }

        List<ChannelDto> list = new ArrayList<ChannelDto>( channels.size() );
        for ( Channel channel : channels ) {
            list.add( toDto( channel ) );
        }

        return list;
    }
}
