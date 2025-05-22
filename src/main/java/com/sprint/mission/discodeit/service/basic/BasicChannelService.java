package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.channel.DuplicateChannelException;
import com.sprint.mission.discodeit.exceptions.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.ReadStatusJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelFindDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

    private final ChannelJPARepository channelJpaRepository;
    private final ReadStatusJPARepository readStatusJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelMapper channelMapper;


    @Override
    @Transactional
    public ChannelResponseDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        log.debug("[Channel][createPrivate] Entity constructed");
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        log.debug("[Channel][createPrivate] Calling channelJpaRepository.save()");
        Channel createdPrivateChannel = channelJpaRepository.save(channel);

        log.debug("[Channel][createPrivate] Calling userJpaRepository.findByIdIn()");
        userJpaRepository.findByIdIn(channelCreatePrivateDto.participantIds()).stream()
                .peek(user -> log.debug("[Channel][createPrivate] ReadStatus entity constructed: userId={}", user.getId()))
                .map(user -> new ReadStatus(user, createdPrivateChannel, createdPrivateChannel.getCreatedAt()))
                .forEach(readStatusJpaRepository::save);

        ChannelResponseDto channelResponse = channelMapper.toDto(createdPrivateChannel);
        log.info("[Channel][createPrivate] Created successfully: channelId={}", channelResponse.id());
        return channelResponse;
    }


    @Override
    @Transactional
    public ChannelResponseDto createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        log.debug("[Channel][createPublic] Calling channelJpaRepository.existsByTypeAndName()");
        if (channelJpaRepository.existsByTypeAndName(ChannelType.PUBLIC, channelCreatePublicDto.name())) {
            log.warn("[Channel][createPublic] A channel already exists: channelName={}", channelCreatePublicDto.name());
            throw new DuplicateChannelException(Map.of("channelName", channelCreatePublicDto.name()));
        }

        log.debug("[Channel][createPublic] Entity constructed");
        Channel createdPublicChannel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(), channelCreatePublicDto.description());
        log.debug("[Channel][createPublic] Calling channelJpaRepository.save()");
        channelJpaRepository.save(createdPublicChannel);
        ChannelResponseDto channelResponse =channelMapper.toDto(createdPublicChannel);
        log.info("[Channel][createPublic] Created successfully: channelId={}", channelResponse.id());
        return channelResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto find(ChannelFindDto channelFindDto) {
        Channel matchingChannel = channelJpaRepository.findById(channelFindDto.channelId())
                .orElse(null);
        return channelMapper.toDto(matchingChannel);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<UUID> matchingChannelIdList = readStatusJpaRepository.findByUser_Id(userId).stream()
                .map(ReadStatus::getChannel)
                .map(BaseEntity::getId)
                .toList();

        return channelJpaRepository
                .findByTypeOrIdIn(ChannelType.PUBLIC, matchingChannelIdList).stream()
                .map(channelMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public ChannelResponseDto update(UUID channelId, ChannelUpdateDto channelUpdateDto) {
        log.debug("[Channel][update] Calling channelJpaRepository.findById(): channelId={}", channelId);
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", channelId)));

        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            log.warn("[Channel][update] Private channels cannot be changed.");
            throw new PrivateChannelUpdateException(Map.of("channelId", channelId));
        }

        log.debug("[Channel][update] Calling updateChannel(): channelId={}", channelId);
        matchingChannel.updateChannel(channelUpdateDto.newName(), channelUpdateDto.newDescription());
        log.debug("[Channel][update] Calling channelJpaRepository.save(): channelId={}", channelId);
        Channel updateChannel = channelJpaRepository.save(matchingChannel);
        log.info("[Channel][update] Updated successfully: channelId={}", channelId);
        return channelMapper.toDto(updateChannel);

    }


    @Override
    @Transactional
    public void delete(UUID channelId) {
        log.debug("[Channel][delete] Calling userJpaRepository.findById()");
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", channelId)));
        channelJpaRepository.delete(matchingChannel);
        log.info("[Channel][delete] Deleted successfully: channelId]{}", channelId);
    }
}
