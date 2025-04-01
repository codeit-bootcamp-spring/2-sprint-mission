package com.sprint.mission.discodeit.core.channel.usecase;


import org.springframework.stereotype.Service;


@Service
public interface ChannelService extends ChannelAccessUseCase, CreateChannelUseCase,
    FindChannelUseCase, UpdateChannelUseCase, DeleteChannelUseCase {

//    void printChannels(UUID serverId);

//    void printUsersInChannel(UUID channelId);


}
