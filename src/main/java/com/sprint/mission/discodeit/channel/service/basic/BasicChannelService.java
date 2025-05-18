package com.sprint.mission.discodeit.channel.service.basic;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.common.constant.ErrorCode.ERROR_CHANNEL_NOT_FOUND;

@Slf4j
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
        log.info("공개 채널 생성 요청: name={}, description={}", channelRegisterRequest.name(), channelRegisterRequest.description());
        Channel channel = new Channel(ChannelType.PUBLIC, channelRegisterRequest.name(), channelRegisterRequest.description());
        Channel savedChannel = channelRepository.save(channel);
        log.info("공개 채널 생성 성공: channelId={}", savedChannel.getId());

        return channelMapper.convertToChannelResult(savedChannel);
    }

    @Transactional
    @Override
    public ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
        log.info("비공개 채널 생성 요청: participantIds={}", privateChannelCreateRequest.participantIds());
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        List<User> members = userRepository.findAllById(privateChannelCreateRequest.participantIds());
        List<ReadStatus> readStatuses = members.stream()
                .map(user -> new ReadStatus(user, savedChannel))
                .toList();
        readStatusRepository.saveAllAndFlush(readStatuses);
        log.info("비공개 채널 생성 성공: channelId={}, 멤버수={}", savedChannel.getId(), members.size());

        return channelMapper.convertToChannelResult(savedChannel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelResult getById(UUID channelId) {
        log.debug("채널 조회 요청: channelId={}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.error("채널 조회 실패: channelId={} (존재하지 않음)", channelId);
                    return new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessage());
                });

        return channelMapper.convertToChannelResult(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelResult> getAllByUserId(UUID userId) {
        log.debug("사용자별 채널 목록 조회 요청: userId={}", userId);
        List<Channel> publicChannels = channelRepository.findChannelByType(ChannelType.PUBLIC);

        List<UUID> privateChannelIds = readStatusRepository.findByUser_Id(userId)
                .stream()
                .map(readStatus -> readStatus.getChannel().getId())
                .toList();
        List<Channel> privateChannels = channelRepository.findAllById(privateChannelIds);
        publicChannels.addAll(privateChannels);
        log.info("사용자별 채널 목록 조회 성공: userId={}, 전체 채널 수={}", userId, publicChannels.size());

        return publicChannels.stream()
                .map(channelMapper::convertToChannelResult)
                .toList();
    }

    @Transactional
    @Override
    public ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        log.info("공개 채널 수정 요청: channelId={}, newName={}, newDescription={}", id, publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("공개 채널 수정 실패: channelId={} (존재하지 않음)", id);
                    return new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessage());
                });

        channel.update(publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        Channel updatedChannel = channelRepository.save(channel);
        log.info("공개 채널 수정 성공: channelId={}", updatedChannel.getId());

        return channelMapper.convertToChannelResult(updatedChannel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.warn("채널 삭제 요청: channelId={}", channelId);
        if (!channelRepository.existsById(channelId)) {
            log.error("채널 삭제 실패: channelId={} (존재하지 않음)", channelId);
            throw new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessage());
        }

        readStatusRepository.deleteAllByChannel_Id(channelId);
        messageRepository.deleteAllByChannel_Id(channelId);

        channelRepository.deleteById(channelId);
        log.info("채널 삭제 성공: channelId={}", channelId);
    }

}
