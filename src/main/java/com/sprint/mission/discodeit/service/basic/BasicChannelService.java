package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelRequest privateRequest) {
    log.debug("비공개 채널 생성 시작: {}", privateRequest);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<UUID> participantIds = privateRequest.participantIds();
    List<ReadStatus> readStatusList = userRepository.findAllById(participantIds).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatusList);
    log.info("비공개 채널 생성 완료: id={}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto create(PublicChannelRequest publicRequest) {
    log.debug("공개 채널 생성 시작: {}", publicRequest);
    Channel channel = new Channel(ChannelType.PUBLIC, publicRequest.name(),
        publicRequest.description());
    channelRepository.save(channel);
    log.info("공개 채널 생성 완료: id={}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> joinedChannelIds = readStatusRepository.findAllChannelIdByUserId(userId);

    List<Channel> channelList = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        joinedChannelIds);
    return channelMapper.toDtoList(channelList);
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, ChannelUpdateRequest updateRequest) {
    log.debug("채널 수정 시작: id={}, {}", id, updateRequest);
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Channel을 찾을 수 없음"));
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("비공개 채널은 수정 불가능");
    }
    String newName = updateRequest.newName();
    String newDescription = updateRequest.newDescription();

    channel.update(newName, newDescription);
    log.info("채널 수정 완료: id={}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    log.debug("채널 삭제 시작: id={}", channelId);
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + "에 해당하는 Channel을 찾을 수 없음");
    }
    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
    log.debug("채널 삭제 완료: id={}", channelId);
  }
}
