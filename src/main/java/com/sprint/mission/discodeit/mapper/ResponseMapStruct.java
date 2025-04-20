package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseMapStruct {

    @Mapping(target = "online", source = "user", qualifiedByName = "onlineStatus")
    UserResponseDto toUserDto(User user);

    @Mapping(target = "userId", source = "user.id")
    UserStatusResponseDto toUserStatusDto(UserStatus userStatus);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "channelId", source = "channel.id")
    ReadStatusResponseDto toReadStatusDto(ReadStatus readStatus);

    @Mapping(target = "channelId", source = "channel.id")
    MessageResponseDto toMessageDto(Message message);

    BinaryContentResponseDto toBinaryContentDto(BinaryContent binaryContent);


    @Named("onlineStatus")
    static Boolean onlineStatus(User user) {
        return user.getStatus() != null ? user.getStatus().currentUserStatus() : null;
    }
}
