/*
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository jcfChannelRepository;
    private final MessageRepository jcfMessageRepository;
    private final UserRepository jcfUserRepository;

    public JCFChannelService(UserRepository jcfUserRepository, ChannelRepository jcfChannelRepository, MessageRepository jcfMessageRepository) {
        this.jcfUserRepository = jcfUserRepository;
        this.jcfChannelRepository = jcfChannelRepository;
        this.jcfMessageRepository = jcfMessageRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);      //channelName에 대한 유효성 검증은 Channel 생성자에게 맡긴다.
        jcfChannelRepository.add(newChannel);
        return newChannel;
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return this.jcfChannelRepository.findById(channelId);
    }

    @Override
    public Map<UUID, Channel> readAllChannels() {
        return jcfChannelRepository.getAll();
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        ChannelService.validateChannelId(channelId, this.jcfChannelRepository);
        return jcfMessageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        jcfChannelRepository.updateChannelName(channelId, newChannelName);
    }

    @Override
    public void addChannelParticipant(UUID channelId, UUID newParticipantId) {        // channelId 검증은 channelRepository 에서 수행
        UserService.validateUserId(newParticipantId, this.jcfUserRepository);
        jcfChannelRepository.addParticipant(channelId, newParticipantId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        jcfChannelRepository.deleteById(channelId);
    }
}
*/
