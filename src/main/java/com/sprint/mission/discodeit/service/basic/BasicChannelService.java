package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequestDto requestDto) {
        Channel channel = new Channel(ChannelType.PUBLIC, requestDto.name(), requestDto.description());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        List<UUID> participantIds = requestDto.participantIds();
        validateParticipantExistence(participantIds);

        Channel channel = new Channel(ChannelType.PRIVATE);
        channel = channelRepository.save(channel);

        UUID channelId = channel.getId();
        createParticipantReadStatuses(participantIds, channelId);

        return channel;
    }

    private void createParticipantReadStatuses(List<UUID> participantIds, UUID channelId) {
        participantIds.forEach(userId ->
                readStatusRepository.save(new ReadStatus(userId, channelId)));
    }

    private void validateParticipantExistence(List<UUID> participantIds) {
        participantIds.forEach(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchElementException("유저가 없습니다")));
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }
}
