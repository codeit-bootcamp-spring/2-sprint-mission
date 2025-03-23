package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapping.ChannelMapping;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Autowired
    public BasicChannelService(
            @Qualifier("basicChannelRepository") ChannelRepository channelRepository,
            @Qualifier("basicMessageRepository") MessageRepository messageRepository,
            @Qualifier("basicReadStatusRepository") ReadStatusRepository readStatusRepository,
            @Qualifier("basicUserRepository") UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto) {
        // 채널 생성
        Channel channel;
        if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
            channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getParticipantIds());
        } else {
            channel = new Channel(dto.getOwnerId(), dto.getParticipantIds());
        }
        
        channelRepository.register(channel);
        
        // 모든 참여자에 대한 ReadStatus 생성
        for (UUID userId : dto.getParticipantIds()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getChannelId(), null);
            readStatusRepository.register(readStatus);
        }
        
        return ChannelMapping.INSTANCE.channelToResponse(channel);
    }

    @Override
    public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto) {
        // 채널명 중복 체크
        checkChannelNameDuplication(dto.getChannelName());
        
        // 채널 생성
        Channel channel;
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getDescription());
        } else {
            channel = new Channel(dto.getChannelName(), dto.getOwnerId());
        }
        
        // 채널 저장
        channelRepository.register(channel);

        return ChannelMapping.INSTANCE.channelToResponse(channel);
    }

    @Override
    public ChannelDto.Response findById(UUID channelId) {
        // findChannelById -> findById로 변경 (테스트와 일치)
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found with ID: " + channelId));
        
        // Response 객체 생성
        ChannelDto.Response response = ChannelMapping.INSTANCE.channelToResponse(channel);
        
        // 최근 메시지 시간 정보 추가
        findLatestMessageTimestamp(channelId).ifPresent(response::setLastMessageTime
        );
        
        return response;
    }

    @Override
    public List<ChannelDto.Response> findAllByUserId(UUID userId) {
        // 모든 채널 ID 목록
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
                findLatestMessageTimestamp(channelId).ifPresent(timestamp -> 
                    response.setLastMessageTime(timestamp)
                );
                
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
            // findChannelById -> findById로 변경 (테스트와 일치)
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (!channelOpt.isPresent()) continue;
            
            Channel channel = channelOpt.get();
            
            if (channel.isPublic()) {
                ChannelDto.Response response = ChannelMapping.INSTANCE.channelToResponse(channel);
                
                // 최근 메시지 시간 정보 추가
                findLatestMessageTimestamp(channelId).ifPresent(timestamp -> 
                    response.setLastMessageTime(timestamp)
                );
                
                result.add(response);
            }
        }
        
        return result;
    }

    @Override
    public ChannelDto.Response updateChannel(ChannelDto.Update dto) {
        // findChannelById -> findById로 변경 (테스트와 일치)
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found with ID: " + dto.getChannelId()));
        
        // PRIVATE 채널은 수정 불가
        if (channel.isPrivate()) {
            throw new RuntimeException("PRIVATE 채널은 수정 불가");
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
    public boolean deleteChannel(UUID channelId) {
        Optional<Channel> channelOpt = channelRepository.findById(channelId);
        if (channelOpt.isEmpty()) {
            return false;
        }
        
        Channel channel = channelOpt.get();
        
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
        return channelRepository.deleteChannel(channelId);
    }

    // 채널명 중복 체크 로직
    private void checkChannelNameDuplication(String channelName) {

        channelRepository.findByName(channelName).ifPresent(channel -> {
            throw new IllegalArgumentException("이미 존재하는 채널명입니다: " + channelName);
        });
    }
    

    private Optional<ZonedDateTime> findLatestMessageTimestamp(UUID channelId) {
        // MessageRepository에 findLatestByChannelId 메서드가 있으면 사용, 없으면 직접 구현
        List<Message> channelMessages = messageRepository.findAllByChannelId(channelId);
        if (channelMessages.isEmpty()) {
            return Optional.empty();
        }
        
        return channelMessages.stream()
                .map(Message::getCreatedAt)
                .max(ZonedDateTime::compareTo);
    }
    

    private void deleteAllMessagesByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        for (Message message : messages) {
            messageRepository.deleteMessage(message.getId());
        }
    }
}

