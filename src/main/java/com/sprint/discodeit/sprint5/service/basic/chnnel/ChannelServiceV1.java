package com.sprint.discodeit.sprint5.service.basic.chnnel;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelServiceV1 {
    ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto);
    ChannelResponseDto createPublicChannel(PublicChannelCreateRequestDto requestDto);
    Channel update(String channelId,ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
    ChannelFindResponseDto findChannelById(UUID channelId);
    List<ChannelSummaryResponseDto> findAllByUserId(UUID userId);
    List<ChannelSummaryResponseDto> findAll();
}
