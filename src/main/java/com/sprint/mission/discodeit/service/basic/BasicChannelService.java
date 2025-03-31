package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelResponseDTO createPublic(CreatePublicChannelDTO createPublicChannelDTO) {
        Channel channel = new Channel(ChannelType.PUBLIC, createPublicChannelDTO.getName(), createPublicChannelDTO.getDescription());
        channel = channelRepository.save(channel);

        // channel을 만들 때는 message와 userid가 아직 할당되지 않는다.
        return new ChannelResponseDTO(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                null, null
        );
    }

    @Override
    public ChannelResponseDTO createPrivate(CreatePrivateChannelDTO createPrivateChannelDTO){
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channel = channelRepository.save(channel);

        List<UUID> userIds = createPrivateChannelDTO.getUserIds();
        if(userIds != null){
            for(UUID userId : userIds){
                ReadStatus readStatus = new ReadStatus(
                        UUID.randomUUID(),
                        userId,
                        channel.getId(),
                        Instant.now(),
                        Instant.now());
                readStatusRepository.save(readStatus);
            }
        }
        return new ChannelResponseDTO(
                channel.getId(),
                null, null,
                channel.getType(),
                null,
                userIds
        );
    }

    @Override
    public ChannelResponseDTO find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        // messageRepository.findAll()로 메시지 가져와서 channel을 찾는다.
        // channel에 가장 최신 message를 찾는다.
        List<Message> allMessages = messageRepository.findAll();
        Message recentMessage = null;
        for(Message message : allMessages){
            if(message.getChannelId().equals(channel.getId())){
                // recentMessage가 없거나, message가 더 최근 message라면 갱신
                if(recentMessage == null || message.getCreatedAt().isAfter(recentMessage.getCreatedAt())){
                    recentMessage = message;
                }
            }
        }
        // 최근 메시지 시간 저장
        Instant lastMessageTime = (recentMessage != null) ? recentMessage.getCreatedAt() : null;
        // private일 경우 참여자 목록을 구한다.
        // 모든 user를 순회하며 해당 채널에 참여한 user들을 골라낸다.
        List<UUID> userIds = null;
        if(channel.getType() == ChannelType.PRIVATE){
            userIds = new ArrayList<>();
            List<User> allUsers = userRepository.findAll();
            for(User user : allUsers){
                ReadStatus readStatus = readStatusRepository.findByUserAndChannel(user.getId(), channelId);
                if (readStatus != null) {
                    userIds.add(user.getId());
                }
            }
        }

        return new ChannelResponseDTO(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                lastMessageTime,
                userIds
        );
    }

    @Override
    public List<ChannelResponseDTO> findAll() {
        List<Channel> allChannels = channelRepository.findAll();
        List<ChannelResponseDTO> result = new ArrayList<>();

        for(Channel channel : allChannels){
            Instant lastMessageTime = findLastMessageTime(channel.getId());

            // private 채널 참여자 목록 조회
            List<UUID> participantsUserIds = null;
            if(channel.getType() == ChannelType.PRIVATE){
                participantsUserIds = findParticipantUserIds(channel.getId());
            }
            ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO(
                    channel.getId(),
                    channel.getName(),
                    channel.getDescription(),
                    channel.getType(),
                    lastMessageTime,
                    participantsUserIds
            );
            result.add(channelResponseDTO);
        }
        return result;
    }

    @Override
    public void update(UpdateChannelDTO updateChannelDTO) {
        Channel channel = channelRepository.findById(updateChannelDTO.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + updateChannelDTO.getChannelId() + " not found"));

        if(channel.getType() == ChannelType.PRIVATE){
            throw new UnsupportedOperationException("Cannot update a private channel");
        }

        channel.update(updateChannelDTO.getName(), updateChannelDTO.getDescription());
        channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        List<Message> allMessages = messageRepository.findAll();
        for(Message message : allMessages){
            if(message.getChannelId().equals(channel.getId())){
                allMessages.remove(message);
            }
        }

        List<User> allUsers = userRepository.findAll();
        for(User user : allUsers){
            ReadStatus readStatus = readStatusRepository.findByUserAndChannel(user.getId(), channelId);
            if(readStatus != null){
                readStatusRepository.delete(readStatus);
            }
        }
        channelRepository.deleteById(channelId);
    }

    private Instant findLastMessageTime(UUID channelId) {
        List<Message> allMessages = messageRepository.findAll();
        Message recent = null;
        for (Message m : allMessages) {
            if (m.getChannelId().equals(channelId)) {
                if (recent == null || m.getCreatedAt().isAfter(recent.getCreatedAt())) {
                    recent = m;
                }
            }
        }
        return (recent != null) ? recent.getCreatedAt() : null;
    }

    private List<UUID> findParticipantUserIds(UUID channelId) {
        List<UUID> userIds = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            ReadStatus rs = readStatusRepository.findByUserAndChannel(user.getId(), channelId);
            if (rs != null) {
                userIds.add(user.getId());
            }
        }
        return userIds;
    }
}
