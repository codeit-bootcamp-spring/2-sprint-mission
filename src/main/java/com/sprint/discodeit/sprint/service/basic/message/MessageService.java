package com.sprint.discodeit.sprint.service.basic.message;

import com.sprint.discodeit.sprint.domain.dto.PaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelMessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.CursorPaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateResponseDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import org.springframework.data.domain.Pageable;

public interface MessageService {


    MessageResponseDto create(Long channelId, MessageRequestDto messageRequestDto);
    CursorPaginatedResponse<ChannelMessageResponseDto> findByChannelCursor(Long channelId, Long lastMessageId, int size);
    PaginatedResponse<ChannelMessageResponseDto> findChannel(Long channelId, Pageable page);
    MessageUpdateResponseDto update(Long messageId, MessageUpdateRequestDto messageUpdateRequestDto);
    void delete(Long messageId);
}
