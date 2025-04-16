package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.ReadStatusJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelJPARepository channelJpaRepository;
    private final ReadStatusJPARepository readStatusJpaRepository;
    private final MessageJPARepository messageJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelMapper channelMapper;


    @Override
    @Transactional
    public ChannelResponseDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdPirvateChannel = channelJpaRepository.save(channel);
        
        userJpaRepository.findByIdIn(channelCreatePrivateDto.participantIds()).stream()
                .map(user -> new ReadStatus(user, createdPirvateChannel, createdPirvateChannel.getCreatedAt()))
                .forEach(readStatusJpaRepository::save);

        return channelMapper.toDto(createdPirvateChannel);
    }


    @Override
    @Transactional
    public ChannelResponseDto createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        if (channelJpaRepository.existsByTypeAndName(ChannelType.PUBLIC, channelCreatePublicDto.name())) {
            throw new InvalidInputException("A channel already exists.");
        }

        Channel createdPublicChannel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(), channelCreatePublicDto.description());
        channelJpaRepository.save(createdPublicChannel);
        return channelMapper.toDto(createdPublicChannel);
    }


    @Override
    public ChannelResponseDto find(ChannelFindDto channelFindDto) {
        Channel matchingChannel = channelJpaRepository.findById(channelFindDto.channelId())
                .orElse(null);

        return channelMapper.toDto(matchingChannel);
    }


    @Override
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
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));

        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new InvalidInputException("Private channels cannot be changed.");
        }
        matchingChannel.updateChannel(channelUpdateDto.newName(), channelUpdateDto.newDescription());
        Channel updateChannel = channelJpaRepository.save(matchingChannel);

        return channelMapper.toDto(updateChannel);

    }


    @Override
    @Transactional
    public void delete(UUID channelId) {
        Channel matchingChannel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));
        channelJpaRepository.delete(matchingChannel);
    }
}
