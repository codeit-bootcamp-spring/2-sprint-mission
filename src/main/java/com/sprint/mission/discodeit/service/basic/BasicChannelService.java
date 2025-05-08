package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private static final Logger logger = LoggerFactory.getLogger(BasicChannelService.class);

    private final ChannelJPARepository channelJpaRepository;
    private final ReadStatusJPARepository readStatusJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelMapper channelMapper;


    @Override
    @Transactional
    public ChannelResponseDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        logger.debug("[Channel][createPrivate] Entity constructed");
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        logger.debug("[Channel][createPrivate] Calling channelJpaRepository.save()");
        Channel createdPrivateChannel = channelJpaRepository.save(channel);

        logger.debug("[Channel][createPrivate] Calling userJpaRepository.findByIdIn()");
        userJpaRepository.findByIdIn(channelCreatePrivateDto.participantIds()).stream()
                .peek(user -> logger.debug("[Channel][createPrivate] ReadStatus entity constructed: userId={}", user.getId()))
                .map(user -> new ReadStatus(user, createdPrivateChannel, createdPrivateChannel.getCreatedAt()))
                .forEach(readStatusJpaRepository::save);

        ChannelResponseDto channelResponse = channelMapper.toDto(createdPrivateChannel);
        logger.info("[Channel][createPrivate] Created successfully: channelId={}", channelResponse.id());
        return channelResponse;
    }


    @Override
    @Transactional
    public ChannelResponseDto createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        logger.debug("[Channel][createPublic] Calling channelJpaRepository.existsByTypeAndName()");
        if (channelJpaRepository.existsByTypeAndName(ChannelType.PUBLIC, channelCreatePublicDto.name())) {
            logger.warn("[Channel][createPublic] A channel already exists: channelName={}", channelCreatePublicDto.name());
            throw new InvalidInputException("A channel already exists");
        }

        logger.debug("[Channel][createPublic] Entity constructed");
        Channel createdPublicChannel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(), channelCreatePublicDto.description());
        logger.debug("[Channel][createPublic] Calling channelJpaRepository.save()");
        channelJpaRepository.save(createdPublicChannel);
        ChannelResponseDto channelResponse =channelMapper.toDto(createdPublicChannel);
        logger.info("[Channel][createPublic] Created successfully: channelId={}", channelResponse.id());
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
        logger.debug("[Channel][update] Calling channelJpaRepository.findById(): channelId={}", channelId);
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));

        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            logger.warn("[Channel][update] Private channels cannot be changed.");
            throw new InvalidInputException("Private channels cannot be changed.");
        }

        logger.debug("[Channel][update] Calling updateChannel(): channelId={}", channelId);
        matchingChannel.updateChannel(channelUpdateDto.newName(), channelUpdateDto.newDescription());
        logger.debug("[Channel][update] Calling channelJpaRepository.save(): channelId={}", channelId);
        Channel updateChannel = channelJpaRepository.save(matchingChannel);
        logger.info("[Channel][update] Updated successfully: channelId={}", channelId);
        return channelMapper.toDto(updateChannel);

    }


    @Override
    @Transactional
    public void delete(UUID channelId) {
        logger.debug("[Channel][delete] Calling userJpaRepository.findById()");
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));
        channelJpaRepository.delete(matchingChannel);
        logger.info("[Channel][delete] Deleted successfully: channelId]{}", channelId);
    }
}
