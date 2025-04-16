package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    BinaryContentMapper binaryContentMapper;
    UserMapper userMapper;

    public MessageDto toDto(Message message) {
        List<BinaryContentDto> binaryContentDtos = new ArrayList<>();
        List<BinaryContent> binaryContentList = Optional.ofNullable(message.getAttachments())
            .orElse(new ArrayList<>());
        if (!binaryContentList.isEmpty() && binaryContentList.size() >= 2) {
            for (BinaryContent binaryContent : binaryContentList) {
                BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
                binaryContentDtos.add(binaryContentDto);
            }
        }
        return new MessageDto(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            message.getChannel().getId(),
            userMapper.toDto(message.getAuthor()),
            binaryContentDtos

        );

    }

}
