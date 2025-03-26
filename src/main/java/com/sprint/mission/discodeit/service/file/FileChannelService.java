/*
package com.sprint.mission.discodeit.service.file;

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

public class FileChannelService implements ChannelService {
    private final ChannelRepository fileChannelRepository;
    private final MessageRepository fileMessageRepository;
    private final UserRepository fileUserRepository;

    public FileChannelService(UserRepository fileUserRepository, ChannelRepository fileChannelRepository, MessageRepository fileMessageRepository) {
        this.fileUserRepository = fileUserRepository;
        this.fileChannelRepository = fileChannelRepository;
        this.fileMessageRepository = fileMessageRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);      //channelName에 대한 유효성 검증은 Channel 생성자에게 맡긴다.
        fileChannelRepository.add(newChannel);
        return newChannel;
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return this.fileChannelRepository.findById(channelId);
    }

    @Override
    public Map<UUID, Channel> readAllChannels() {
        return fileChannelRepository.getAll();
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        ChannelService.validateChannelId(channelId, this.fileChannelRepository);
        return fileMessageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        fileChannelRepository.updateChannelName(channelId, newChannelName);
    }

    @Override
    public void addChannelParticipant(UUID channelId, UUID newParticipantId) {        // channelId 검증은 channelRepository 에서 수행
        UserService.validateUserId(newParticipantId, this.fileUserRepository);
        fileChannelRepository.addParticipant(channelId, newParticipantId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        fileChannelRepository.deleteById(channelId);
    }
}
*/
