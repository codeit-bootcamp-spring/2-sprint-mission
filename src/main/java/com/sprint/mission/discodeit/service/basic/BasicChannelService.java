package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.channel.*;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final MessageRepository messageRepository;

    @Override
    public ChannelDTO createPublicChannel(CreateChannelParam createChannelParam) {
        Channel channel = createPublicChannelEntity(createChannelParam);
        channelRepository.save(channel);
        return entityToDTO(channel);
    }

    @Override
    public PrivateChannelDTO createPrivateChannel(CreatePrivateChannelParam createPrivateChannelParam) {
        Channel channel = createPrivateChannelEntity(createPrivateChannelParam);
        channelRepository.save(channel);
        createReadStatusesForUsers(createPrivateChannelParam.userIds(), channel.getId());
        return entityToPrivateDTO(createPrivateChannelParam.userIds(), channel);
    }

    @Override
    public FindChannelDTO find(UUID channelId) {
        Channel channel = findChannelById(channelId);
        Instant latestMessageTime = findMessageLatestTimeInChannel(channelId);
        List<UUID> userIds = getUserIdsFromChannel(channel);
        return entityToFindDTO(channel, latestMessageTime, userIds);
    }

    // PRIVATE의 경우, 참여한 User의 id정보를 포함해야함
    // 요구사항에선 Channel이 userId를 가지지 않고, ReadStatus에서 userId와 channelId를 가지므로
    // channelId로 ReadStatus를 조회하고,
    // ReadStatus에서 userId를 뽑아내어 사용
    @Override
    public List<FindChannelDTO> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        return channels.stream()
                .filter(ch -> ch.getType() == ChannelType.PUBLIC || getUserIdsFromChannel(ch).contains(userId))
                .map(ch -> entityToFindDTO(ch,
                        findMessageLatestTimeInChannel(ch.getId()),
                        getUserIdsFromChannel(ch)))
                .toList();
    }


    @Override
    public UpdateChannelDTO update(UUID id, UpdateChannelParam updateChannelParam) {
        Channel channel = findChannelById(id);
        if(channel.getType() == ChannelType.PRIVATE) {
            throw RestExceptions.UNAUTHORIZED_PRIVATE_CHANNEL;
        }
        channel.update(updateChannelParam.name(), updateChannelParam.description());
        Channel updatedChannel = channelRepository.save(channel);
        return entityToUpdateDTO(updatedChannel);
    }

    @Override
    public void delete(UUID channelId) {
        readStatusService.deleteByChannelId(channelId);
        messageRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }


    private Channel createPublicChannelEntity(CreateChannelParam createChannelParam) {
        return Channel.ofPublic(createChannelParam.type(),
                createChannelParam.name(),
                createChannelParam.description());
    }

    // private이므로 name과 description 생략 -> null
    private Channel createPrivateChannelEntity(CreatePrivateChannelParam createPrivateChannelParam) {
        return Channel.ofPrivate(createPrivateChannelParam.type());
    }

    private ChannelDTO entityToDTO(Channel channel) {
        return new ChannelDTO(channel.getId(),channel.getCreatedAt(),channel.getUpdatedAt(),channel.getType(), channel.getName(), channel.getDescription());
    }

    private PrivateChannelDTO entityToPrivateDTO(List<UUID> userIds, Channel channel) {
        return new PrivateChannelDTO(channel.getId(),channel.getCreatedAt(),channel.getUpdatedAt(),channel.getType(), channel.getName(), channel.getDescription(), userIds);
    }

    private void createReadStatusesForUsers(List<UUID> userIds, UUID channelId) {
        List<CreateReadStatusParam> createReadStatusParams = userIds.stream()
                        .map(userId -> new CreateReadStatusParam(userId, channelId)) // userId와 channelId를 사용하여 createReadStatus DTO 생성
                        .toList();
        // DTO를 이용해 readStatus 생성
        createReadStatusParams.forEach(cr -> readStatusService.create(cr));
    }

    private UpdateChannelDTO entityToUpdateDTO(Channel channel) {
        return new UpdateChannelDTO(channel.getId(), channel.getUpdatedAt(), channel.getType(), channel.getName(), channel.getDescription());
    }

    private FindChannelDTO entityToFindDTO(Channel channel, Instant latestMessageTime, List<UUID> userIds) {
        return new FindChannelDTO(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), latestMessageTime, userIds);
    }

    // PRIVATE 채널일때만 userId 가져오고 아닐떈 빈리스트 반환
    // 요구사항에선 Channel이 userId를 가지지 않으므로, channelId로 ReadStatus를 조회하고,
    // ReadStatus에서 userId를 뽑아내어 사용
    private List<UUID> getUserIdsFromChannel(Channel channel) {
        return channel.getType() == ChannelType.PRIVATE ?
                readStatusService.findAllByChannelId(channel.getId())
                        .stream().map(rs -> rs.userId())
                        .toList() :
                Collections.emptyList();
    }

    private Channel findChannelById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> RestExceptions.CHANNEL_NOT_FOUND);
    }

    private Instant findMessageLatestTimeInChannel(UUID channelId) {
        // 채널에 메시지가 없다면 null 반환
        if(messageRepository.findAllByChannelId(channelId).isEmpty()) {
            return null;
        }
        return messageRepository.findLatestMessageTimeByChannelId(channelId)
                .orElseThrow(() -> RestExceptions.MESSAGE_NOT_FOUND);
    }


}
