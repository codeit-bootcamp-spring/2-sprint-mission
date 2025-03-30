package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.CreatePrivateChannelException;
import com.sprint.mission.discodeit.exception.channel.CreatePublicChannelException;
import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.provider.ChannelReadStrategyProvider;
import com.sprint.mission.discodeit.provider.ChannelUpdaterProvider;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.updater.channel.ChannelUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelReadStrategyProvider strategyProvider;
    private final ChannelUpdaterProvider updaterProvider;

    @Override
    public UUID createPrivateChannel(PrivateChannelCreateRequest privateChannelCreateRequest) {
        try {
            Channel newChannel = new Channel(ChannelType.PRIVATE);      // private channel 생성자 호출
            this.channelRepository.add(newChannel);
            this.readStatusRepository.addChannelIdMap(newChannel.getId());
            // for 문이 transaction 처리가 간편하다 하여 stream 사용X
            for (UUID userId : privateChannelCreateRequest.users()) {
                UserService.validateUserId(userId, userRepository);             // privateChannelCreateRequest의 userId들에 대한 검증 (따로 앞쪽에 빼는게 좋나?)
                this.readStatusRepository.add(new ReadStatus(userId, newChannel.getId()));
                newChannel.addParticipant(userId);
            }

            this.messageRepository.addChannelIdToChannelIdMessage(newChannel.getId());      // messageRepository의 ChannelIdMessage 와의 동기화
            return newChannel.getId();
        } catch (NoSuchElementException e) {
            throw new CreatePrivateChannelException("존재하지 않는 userId 입니다.", HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new CreatePrivateChannelException("private 채널 생성 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public UUID createPublicChannel(String channelName) {
        try {
            Channel newChannel = new Channel(ChannelType.PUBLIC, channelName);      //channelName에 대한 유효성 검증은 Channel 생성자에게 맡긴다.
            this.channelRepository.add(newChannel);
            this.readStatusRepository.addChannelIdMap(newChannel.getId());
            this.messageRepository.addChannelIdToChannelIdMessage(newChannel.getId());      // messageRepository의 ChannelIdMessage 와의 동기화
            return newChannel.getId();
        } catch (Exception e) {
            throw new CreatePublicChannelException("public 채널 생성 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);

        }
    }

    @Override
    public ChannelReadResponse readChannel(UUID channelId) {
        Channel findChannel = this.channelRepository.findById(channelId);
        return strategyProvider.getChannelReadStrategy(findChannel.getChannelType()).toDto(findChannel);
    }

    @Override
    public List<ChannelReadResponse> findAllByUserId(UUID userId) {
        UserService.validateUserId(userId, this.userRepository);
        return channelRepository.findAllByUserId(userId).stream()
                .map(channel -> strategyProvider.getChannelReadStrategy(channel.getChannelType()).toDto(channel))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        ChannelService.validateChannelId(channelId, this.channelRepository);
        return this.messageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannel(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        Channel findChannel = this.channelRepository.findById(channelId);
        List<ChannelUpdater> applicableUpdaters = updaterProvider.getApplicableUpdaters(findChannel, channelUpdateRequest);
        applicableUpdaters.forEach(updater -> updater.update(channelId, channelUpdateRequest, this.channelRepository));
    }

    // 삭제해야 될지도? 추후 필요할지도 모르니 일단 남겨두겠음 (스프린트미션3 기준 public은 참여자 정보가 필요없고, private은 참여자 정보가 필요함. 하지만 private은 생성 후 수정 불가능)
    @Override
    public void addChannelParticipant(UUID channelId, UUID newParticipantId) {        // channelId 검증은 channelRepository 에서 수행
        UserService.validateUserId(newParticipantId, this.userRepository);
        this.channelRepository.addParticipant(channelId, newParticipantId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        this.channelRepository.deleteById(channelId);
        this.messageRepository.findMessageListByChannelId(channelId).forEach(message -> this.messageRepository.deleteById(message.getId()));
        this.readStatusRepository.deleteByChannelId(channelId);
    }
}
