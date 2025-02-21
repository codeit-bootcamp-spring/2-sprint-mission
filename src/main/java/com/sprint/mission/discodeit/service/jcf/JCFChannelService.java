package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayDeque;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private ChannelRepository channelRepository;

    public JCFChannelService() {
        this.channelRepository = new ChannelRepository();
    }

    public void createChannel(Channel newChannel) {
        if (newChannel == null) {
            throw new IllegalArgumentException("생성할 채널이 null 입니다!!!");
        }
        channelRepository.addChannel(newChannel);
    }
    public Channel readChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력받은 channelId 가 null 입니다!!!");
        }
        return channelRepository.findChannelById(channelId);
    }

    public ArrayDeque<Channel> readAllChannels() {
        return channelRepository.getChannels();
    }

    public void updateChannelName(UUID channelId, String newChannelName) {
        readChannel(channelId).updateChannelName(newChannelName);
    }

    public void addChannelParticipant(UUID channelId, User newParticipant) {
        //newParticipant 가 userRepository 에 있는 user 인지는 다른데서 확인 필요?
        channelRepository.addParticipant(channelId, newParticipant);
    }

    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("삭제하려는 channelId 가 null 입니다!!!");
        }
        channelRepository.deleteChannel(channelId);
    }
}
