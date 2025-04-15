package com.sprint.discodeit.sprint.service.basic.chnnel;

import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelPrivateUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelPublicUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelUpdateResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PublicChannelCreateRequestDto;
import java.util.List;

public interface ChannelService {
    ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto);
    ChannelResponseDto createPublicChannel(PublicChannelCreateRequestDto requestDto);
    ChannelUpdateResponseDto privateUpdate(ChannelPrivateUpdateRequestDto channelUpdateRequestDto, Long channelId);
    ChannelUpdateResponseDto publicUpdate(ChannelPublicUpdateRequestDto channelUpdateRequestDto, Long channelId);
    void delete(Long channelId);
    List<ChannelSummaryResponseDto> findAllByUsersId(Long usersId);
    ChannelFindResponseDto findChannelById(Long channelId);
}
