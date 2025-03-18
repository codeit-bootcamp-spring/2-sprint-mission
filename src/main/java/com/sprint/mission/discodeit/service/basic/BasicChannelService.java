package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreateChannelParam;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelParam;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
        validateChannelField(createChannelParam);
        Channel channel = createPublicChannelEntity(createChannelParam);
        channelRepository.save(channel);
        return channelEntityToDTO(channel);
    }

    @Override
    public ChannelDTO createPrivateChannel(List<UUID> userIds, CreateChannelParam createChannelParam) {
        validateChannelField(createChannelParam);
        Channel channel = createPrivateChannelEntity(createChannelParam);
        channelRepository.save(channel);
        createReadStatusesForUsers(userIds, channel.getId());
        return channelEntityToDTO(channel);
    }

    @Override
    public FindChannelDTO find(UUID channelId) {
        Channel channel = findChannelById(channelId);
        Instant latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(channelId);
        List<UUID> userIds = getUserIdsFromChannel(channel);
        return channelEntityToFindDTO(channel, latestMessageTime, userIds);
    }

    // PRIVATE의 경우, 참여한 User의 id정보를 포함해야함
    // 요구사항에선 Channel이 userId를 가지지 않으므로, channelId로 ReadStatus를 조회하고,
    // ReadStatus에서 userId를 뽑아내어 사용
    @Override
    public List<FindChannelDTO> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        return channels.stream()
                .filter(ch -> ch.getType() == ChannelType.PUBLIC || getUserIdsFromChannel(ch).contains(userId))
                .map(ch -> channelEntityToFindDTO(ch,
                        messageRepository.findLatestMessageTimeByChannelId(ch.getId()),
                        getUserIdsFromChannel(ch)))
                .toList();
    }

    @Override
    public UUID update(UpdateChannelParam updateChannelParam) {
        Channel channel = findChannelById(updateChannelParam.id());
        if(channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        channel.update(updateChannelParam.name(), updateChannelParam.description());
        channelRepository.save(channel);
        return channel.getId();
    }

    @Override
    public void delete(UUID channelId) {
        readStatusService.deleteByChannelId(channelId);
        messageRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }

    private void validateChannelField(CreateChannelParam createChannelParam) {
        if (createChannelParam.type() == null || createChannelParam.name() == null || createChannelParam.name().isBlank() || createChannelParam.description() == null || createChannelParam.description().isBlank()) {
            throw new IllegalArgumentException("type, name, description은 필수 입력값입니다.");
        }
    }

    private Channel createPublicChannelEntity(CreateChannelParam createChannelParam) {
        return Channel.builder()
                .type(createChannelParam.type())
                .description(createChannelParam.description())
                .name(createChannelParam.name())
                .build();
    }

    // private이므로 name과 description 생략 -> null
    private Channel createPrivateChannelEntity(CreateChannelParam createChannelParam) {
        return Channel.builder()
                .type(createChannelParam.type())
                .build();
    }

    private ChannelDTO channelEntityToDTO(Channel channel) {
        return ChannelDTO.builder()
                .id(channel.getId())
                .updatedAt(channel.getUpdatedAt())
                .createdAt(channel.getCreatedAt())
                .description(channel.getDescription())
                .name(channel.getName())
                .type(channel.getType())
                .build();
    }

    private void createReadStatusesForUsers(List<UUID> userIds, UUID channelId) {
        List<CreateReadStatusParam> createReadStatusParams =
                userIds.stream()
                        .map(userId -> CreateReadStatusParam.builder()
                                .userId(userId)
                                .channelId(channelId)
                                .build())
                        .toList();
        createReadStatusParams.forEach(cr -> readStatusService.create(cr));
    }

    private FindChannelDTO channelEntityToFindDTO(Channel channel, Instant latestMessageTime, List<UUID> userIds) {
        return FindChannelDTO.builder()
                .id(channel.getId())
                .description(channel.getDescription())
                .name(channel.getName())
                .type(channel.getType())
                .lastMessageAt(latestMessageTime)
                .userIds(userIds)
                .build();
    }

    // PRIVATE 채널일때만 userId 가져오고 아닐떈 빈리스트 반환
    private List<UUID> getUserIdsFromChannel(Channel channel) {
        return channel.getType() == ChannelType.PRIVATE ?
                readStatusService.findAllByChannelId(channel.getId())
                        .stream().map(rs -> rs.userId())
                        .toList() :
                Collections.emptyList();
    }

    private Channel findChannelById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "채널이 존재하지 않습니다."));
    }


}
