package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;

@Mapper(config = CentralMapperConfig.class)
public interface UserStatusMapping {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  StatusDto.Summary userStatusToSummary(UserStatus userStatus);

  @Mapping(target = "id", source = "userStatus.id")
  @Mapping(target = "message", expression = "java(message)")
  StatusDto.ResponseDelete userStatusToResponse(UserStatus userStatus, @Context String message);
}
