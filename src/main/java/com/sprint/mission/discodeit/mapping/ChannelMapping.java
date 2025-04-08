package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.common.ChannelInfo;
import com.sprint.mission.discodeit.dto.common.TimeStamps;
import com.sprint.mission.discodeit.dto.common.UserChannels;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface ChannelMapping {

  @Mapping(target = "compositeIdentifier.id", source = "channelId")
  @Mapping(target = "compositeIdentifier.ownerId", source = "ownerId")
  @Mapping(target = "channelInfo.channelName", source = "channelName")
  @Mapping(target = "channelInfo.channelType", source = "channelType")
  @Mapping(target = "timeStamps.createdAt", source = "createdAt")
  @Mapping(target = "timeStamps.updatedAt", source = "updatedAt")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ChannelDto.Response channelToResponse(Channel channel);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ChannelDto.Update channelToUpdateDto(Channel channel);


}