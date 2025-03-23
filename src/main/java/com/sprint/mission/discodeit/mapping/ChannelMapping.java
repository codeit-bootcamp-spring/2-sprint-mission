package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface ChannelMapping {
    ChannelMapping INSTANCE = Mappers.getMapper(ChannelMapping.class);
    ChannelDto.Response channelToResponse(Channel channel);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ChannelDto.Update channelToUpdateDto(Channel channel);
    
    default Channel toEntity(ChannelDto.CreatePublic dto) {
        if (dto == null) {
            return null;
        }
        
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            return new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getDescription());
        } else {
            return new Channel(dto.getChannelName(), dto.getOwnerId());
        }
    }
    
    default Channel toEntity(ChannelDto.CreatePrivate dto) {
        if (dto == null) {
            return null;
        }
        
        if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
            return new Channel(dto.getChannelName(), dto.getOwnerId(), 
                              dto.getParticipantIds() != null ? 
                              dto.getParticipantIds() : new HashSet<UUID>());
        } else {
            return new Channel(dto.getOwnerId(), 
                              dto.getParticipantIds() != null ? 
                              dto.getParticipantIds() : new HashSet<UUID>());
        }
    }
}