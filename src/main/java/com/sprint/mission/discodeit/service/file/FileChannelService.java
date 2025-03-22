package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

@RequiredArgsConstructor
public class FileChannelService implements ChannelService {
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
        return channelRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Channel::getCreatedAt))
                .map(channel -> ChannelDto.fromEntity(channel))
                .toList();
    }

    @Override
    public ChannelDto updateName(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }

        return ChannelDto.fromEntity(channelRepository.updateName(id, name));
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }

    @Override
    public ChannelDto addMemberToPrivate(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.save(new ReadStatus(friendId, channel.getId()));
        channelRepository.save(channel);

        return ChannelDto.fromEntity(channel);
    }
}
