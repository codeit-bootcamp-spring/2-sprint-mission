package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataConflictException;
import com.sprint.mission.discodeit.exception.ForbiddenException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.ChannelMapping;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;


    //private 명확하므로 읽음상태 생성
    @Override
    public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto) {
        Channel channel;
        if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
            channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getParticipantIds());
        } else {
            channel = new Channel(dto.getOwnerId(), dto.getParticipantIds());
        }

        channelRepository.register(channel);

        // 모든 참여자에 대한 ReadStatus 생성
        for (UUID userId : dto.getParticipantIds()) {
            userRepository.findByUser(userId).get().addBelongChannel(channel.getId());
            ReadStatus readStatus = new ReadStatus(userId, channel.getChannelId(), null);
            readStatusRepository.register(readStatus);
        }
        
        return ChannelMapping.INSTANCE.channelToResponse(channel);
    }

    @Override
    public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto) {

        checkChannelNameDuplication(dto.getChannelName());
        Channel channel;
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getDescription());
        } else {
            channel = new Channel(dto.getChannelName(),dto.getOwnerId());
        }
        userRepository.findByUser(dto.getOwnerId()).get().addBelongChannel(channel.getId());
        // 채널 저장
        channelRepository.register(channel);

        return ChannelMapping.INSTANCE.channelToResponse(channel);
    }

    @Override
    public ChannelDto.Response findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

        ChannelDto.Response response = ChannelMapping.INSTANCE.channelToResponse(channel);

        findLatestMessageTimestamp(channelId).ifPresent(response::setLastMessageTime);
        
        return response;
    }

    @Override
    public List<ChannelDto.Response> findAllByUserId(UUID userId) {

        Set<UUID> allChannelIds = channelRepository.allChannelIdList();
        List<ChannelDto.Response> result = new ArrayList<>();
        
        for (UUID channelId : allChannelIds) {
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (channelOpt.isEmpty()) continue;
            
            Channel channel = channelOpt.get();
            
            // PUBLIC 채널이거나 사용자가 채널 멤버인 경우만 결과에 포함
            if (channel.isPublic() || channel.getUserList().contains(userId)) {
                ChannelDto.Response response = ChannelMapping.INSTANCE.channelToResponse(channel);
                
                // 최근 메시지 시간 정보 추가
                findLatestMessageTimestamp(channelId).ifPresent(response::setLastMessageTime);
                
                result.add(response);
            }
        }
        
        return result;
    }

    @Override
    public List<ChannelDto.Response> findAllPublicChannels() {
        Set<UUID> allChannelIds = channelRepository.allChannelIdList();
        List<ChannelDto.Response> result = new ArrayList<>();
        
        for (UUID channelId : allChannelIds) {
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (channelOpt.isEmpty()) continue;
            
            Channel channel = channelOpt.get();
            
            if (channel.isPublic()) {
                ChannelDto.Response response = ChannelMapping.INSTANCE.channelToResponse(channel);
                
                // 최근 메시지 시간 정보 추가
                findLatestMessageTimestamp(channelId).ifPresent(response::setLastMessageTime);
                
                result.add(response);
            }
        }
        
        return result;
    }

    @Override
    public ChannelDto.Response updateChannel(ChannelDto.Update dto, UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", dto.getChannelId()));
        if(!channel.getOwnerId().equals(dto.getOwnerId())){
            throw new InvalidRequestException("권한이 없습니다");
        }
        // PRIVATE 채널은 수정 불가
        if (channel.isPrivate()) {
            throw new InvalidRequestException("channel", "PRIVATE 채널은 수정할 수 없습니다");
        }
        
        // 채널 정보 업데이트
        if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
            channel.setChannelName(dto.getChannelName());
        }
        
        if (dto.getDescription() != null) {
            channel.setDescription(dto.getDescription());
        }
        
        channelRepository.updateChannel(channel);
        
        return ChannelMapping.INSTANCE.channelToResponse(channel);
    }

    @Override
    public boolean deleteChannel(UUID channelId, UUID ownerId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));
                
        if (!channel.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("channel", "delete");
        }
        
        channelRepository.deleteChannel(channelId);
        
        // 채널의 메시지 직접 삭제
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        for (Message message : messages) {
            messageRepository.deleteMessage(message.getId());
        }
        
        // 채널의 읽음 상태 직접 삭제
        readStatusRepository.deleteAllByChannelId(channelId);
        
        // 사용자의 채널 목록에서 해당 채널 제거
        Set<UUID> userIds = channel.getUserList();
        for (UUID userId : userIds) {
            Optional<User> userOpt = userRepository.findByUser(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.removeBelongChannel(channelId);
                userRepository.updateUser(user);
            }
        }
        
        // 채널 삭제
        return true;
    }
    //유저가 속한 프라이빗+공개채널 모두 반환
    @Override
    public List<ChannelDto.Response> getAccessibleChannels(UUID userId) {
        List<ChannelDto.Response> channels = new ArrayList<>();

        Set<UUID> channelIdList = channelRepository.allChannelIdList();
        for (UUID channelId : channelIdList) {
            Optional<Channel> allChannels = channelRepository.findById(channelId);
            if (allChannels.isPresent() && allChannels.get().getUserList().contains(userId))
                channels.add(ChannelMapping.INSTANCE.channelToResponse(allChannels.get()));
        }
        
        return channels;
    }

    // 채널명 중복 체크 로직
    private void checkChannelNameDuplication(String channelName) {
        channelRepository.findByName(channelName).ifPresent(channel -> {
            throw new DataConflictException("Channel", "name", channelName);
        });
    }
    
    private Optional<ZonedDateTime> findLatestMessageTimestamp(UUID channelId) {
        List<Message> channelMessages = messageRepository.findAllByChannelId(channelId);
        if (channelMessages.isEmpty()) {
            return Optional.empty();
        }
        
        return channelMessages.stream()
                .map(Message::getCreatedAt)
                .max(ZonedDateTime::compareTo);
    }
}


