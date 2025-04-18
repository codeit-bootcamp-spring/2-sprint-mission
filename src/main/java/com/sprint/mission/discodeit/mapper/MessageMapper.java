package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.*;
import java.util.*;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "attachments", expression = "java(mapAttachments(message.getAttachments(), binaryContentMapper))")
    MessageDto toDto(Message message, @Context BinaryContentMapper binaryContentMapper, @Context UserMapper userMapper);

    // context 없는 기본 변환 메서드 추가
    default MessageDto toDto(Message message) {
        return toDto(message, null, null);
    }

    // 리스트 변환도 context 없이
    default List<MessageDto> toDto(List<Message> messages) {
        List<MessageDto> result = new ArrayList<>();
        for (Message m : messages) {
            result.add(toDto(m));
        }
        return result;
    }

    default List<BinaryContentDto> mapAttachments(List<BinaryContent> attachments, BinaryContentMapper binaryContentMapper) {
        if (attachments == null) return Collections.emptyList();
        List<BinaryContentDto> result = new ArrayList<>();
        for (BinaryContent binaryContent : attachments) {
            result.add(binaryContentMapper.toDto(binaryContent));
        }
        return result;
    }
}
