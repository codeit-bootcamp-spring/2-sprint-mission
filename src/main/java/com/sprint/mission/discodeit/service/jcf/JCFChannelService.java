package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.user.UsersDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelDto create(ChannelRegisterDto channelRegisterDto) {
        Channel channel = new Channel(channelRegisterDto.channelType(), channelRegisterDto.name());
        Channel savedChannel = channelRepository.save(channel);

        if (channelRegisterDto.channelType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.create(new ReadStatus(channelRegisterDto.owner().id(), savedChannel.getId()));
        }

        return ChannelDto.fromEntity(savedChannel, UsersDto.fromEntity(findChannelUsers(channel)));
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        return ChannelDto.fromEntity(channel, UsersDto.fromEntity(findChannelUsers(channel)));
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Channel::getCreatedAt))
                .map(channel -> ChannelDto.fromEntity(channel, UsersDto.fromEntity(findChannelUsers(channel))))
                .toList();
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
    public ChannelDto addMemberToPrivate(UUID id, String friendEmail) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        readStatusRepository.create(new ReadStatus(friend.getId(), channel.getId()));
        channelRepository.save(channel);

        return ChannelDto.fromEntity(channel, UsersDto.fromEntity(findChannelUsers(channel)));
    }

    private List<User> findChannelUsers(Channel channel) {
        return readStatusRepository.findByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(ReadStatus::getCreatedAt))
                .map(readStatus -> userRepository.findById(readStatus.getUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
