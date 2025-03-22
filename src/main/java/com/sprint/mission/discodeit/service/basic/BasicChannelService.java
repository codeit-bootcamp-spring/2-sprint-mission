package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelDto create(ChannelRegisterDto channelRegisterDto) {
        Channel channel = new Channel(channelRegisterDto.channelType(), channelRegisterDto.name());
        Channel savedChannel = channelRepository.save(channel);

        if (channelRegisterDto.channelType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.save(new ReadStatus(channelRegisterDto.owner().id(), savedChannel.getId()));
        }

        return ChannelDto.fromEntity(savedChannel);
    }


    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        return ChannelDto.fromEntity(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<ChannelDto> channels = new ArrayList<>();
        List<ChannelDto> publicChannels = channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .map(ChannelDto::fromEntity)
                .toList();

        List<ChannelDto> privateChannels = readStatusRepository.findByUserId(userId)
                .stream()
                .map(readStatus -> this.findById(readStatus.getChannelId()))
                .toList();

        channels.addAll(publicChannels);
        channels.addAll(privateChannels);

        return channels;
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }

    @Override
    public ChannelDto addMemberToPrivate(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.save(new ReadStatus(friendId, channel.getId()));
        channelRepository.save(channel); // TODO: 3/22/25 필요있는지 확인 필요

        return ChannelDto.fromEntity(channel);
    }
}

