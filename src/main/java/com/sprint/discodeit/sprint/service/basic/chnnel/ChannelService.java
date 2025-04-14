package com.sprint.discodeit.sprint.service.basic.chnnel;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto);
    ChannelResponseDto createPublicChannel(PublicChannelCreateRequestDto requestDto);
    Channel update(String channelId,ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
    ChannelFindResponseDto findChannelById(UUID channelId);
    List<ChannelSummaryResponseDto> findAllByusersId(UUID usersId);
    List<ChannelSummaryResponseDto> findAll();
}
