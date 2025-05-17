package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelRegisterRequest.name(), channelRegisterRequest.description());
        Channel savedChannel = channelRepository.save(channel);

        return channelMapper.convertToChannelResult(savedChannel);
    }

    @Transactional
    @Override
    public ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        List<User> members = userRepository.findAllById(privateChannelCreateRequest.participantIds());
        List<ReadStatus> readStatuses = members.stream()
                .map(user -> new ReadStatus(user, savedChannel))
                .toList();
        readStatusRepository.saveAllAndFlush(readStatuses);

        return channelMapper.convertToChannelResult(savedChannel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelResult getById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        return channelMapper.convertToChannelResult(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelResult> getAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findChannelByType(ChannelType.PUBLIC);

        List<UUID> privateChannelIds = readStatusRepository.findByUser_Id(userId)
                .stream()
                .map(readStatus -> readStatus.getChannel().getId())
                .toList();
        List<Channel> privateChannels = channelRepository.findAllById(privateChannelIds);
        publicChannels.addAll(privateChannels);

        return publicChannels.stream()
                .map(channelMapper::convertToChannelResult)
                .toList();
    }

    @Transactional
    @Override
    public ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        channel.update(publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        Channel updatedChannel = channelRepository.save(channel);

        return channelMapper.convertToChannelResult(updatedChannel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent());
        }

        readStatusRepository.deleteAllByChannel_Id(channelId);
        messageRepository.deleteAllByChannel_Id(channelId);

        channelRepository.deleteById(channelId);
    }

}
