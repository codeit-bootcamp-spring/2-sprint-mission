package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserDto.Summary;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

//유저의
@Mapper(config = CentralMapperConfig.class)
public interface UserMapping {


  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "user.id", target = "id")
  @Mapping(source = "user.createdAt", target = "createdAt")
    // 필요한 다른 필드 매핑 추가
  Summary userToSummary(User user, UserStatus userStatus);

  UserDto.Response userToResponse(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateUserFromDto(UserDto.Update dto, @MappingTarget User user);

  UserDto.Update userToDto(User user);


}
