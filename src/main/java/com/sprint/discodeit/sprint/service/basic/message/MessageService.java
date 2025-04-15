package com.sprint.discodeit.sprint.service.basic.message;

import com.sprint.discodeit.sprint.domain.dto.PaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelMessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import org.springframework.data.domain.Pageable;

public interface MessageService {


    Message create(Long channelId, MessageRequestDto messageRequestDto);
    //Message find(Long messageId);
    PaginatedResponse<ChannelMessageResponseDto> findChannel(Long channelId, Pageable page);
    Message update(Long messageId, MessageUpdateRequestDto messageUpdateRequestDto);
    void delete(Long messageId);
}
