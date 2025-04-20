package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.*;
import java.util.*;


@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "attachments", source = "attachments")
    @Mapping(target = "author", source = "author")
    MessageDto toDto(Message message);

    List<MessageDto> toDto(List<Message> messages);

    default List<BinaryContentDto> mapAttachments(List<BinaryContent> attachments,
        BinaryContentMapper binaryContentMapper) {
        if (attachments == null) {
            return Collections.emptyList();
        }
        List<BinaryContentDto> result = new ArrayList<>();
        for (BinaryContent binaryContent : attachments) {
            result.add(binaryContentMapper.toDto(binaryContent));
        }
        return result;
    }
}
