package com.sprint.mission.discodeit.core.channel.usecase;


import org.springframework.stereotype.Service;


@Service
public interface ChannelService extends CreateChannelUseCase, UpdateChannelUseCase,
    FindChannelUseCase, DeleteChannelUseCase {

}
