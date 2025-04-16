package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

  // intentionally ignored (해당 Entity의 필드를 무시한다는 것을 명시)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(source = "profile", target = "profile")  //@Mapper(uses = ...)를 통해 MapStruct 가 자동 매핑
  @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : false)")
  // expression 은 java 코드 직접 작성이라 user.status 가 아니니 user.getStatus() 로 호출
  UserDto toDto(User user);
}
