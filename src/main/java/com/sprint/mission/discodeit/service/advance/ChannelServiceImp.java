package com.sprint.mission.discodeit.service.advance;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChannelServiceImp implements ChannelService {
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private MessageRepository messageRepository;

    /*
    create 메소드 public과 private으로 나누기
     */
    @Override
    public Channel createPublic(PublicChanRequest publicChanRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, publicChanRequest.name(), publicChanRequest.description());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivate(PrivateChanRequest privateChanRequest) {
        User user = privateChanRequest.user();

        // 1. PRIVATE 채널 생성 (이름과 설명 겹쳐도 되니까 생략?) 채널 아이디만 다르면 됌.
        Channel channel = new Channel(ChannelType.PRIVATE, "비공개 톡방", "설명설명");
        Channel savedChannel = channelRepository.save(channel);

        // 2. 채널에 참여할 유저의 ReadStatus 생성
        ReadStatus readStatus = new ReadStatus(user.getId(), savedChannel.getId());
        readStatusRepository.save(readStatus);

        return savedChannel;
    }

    // 단일 채널 조회: 추가 필드(lastMessageTime, participantUserIds)를 채워 넣어 반환
    @Override
    public Channel find(FindChannelRequest findChannelRequest) {
        Channel channel = channelRepository.findById(findChannelRequest.channelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + findChannelRequest.channelId() + " not found"));

        Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(findChannelRequest.channelId()).orElse(null);
        channel.setLastMessageTime(lastMessageTime);

        if (channel.getType() == ChannelType.PRIVATE) {
            List<UUID> participantUserIds = readStatusRepository.findByChannelId(findChannelRequest.channelId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .collect(Collectors.toList());
            channel.setParticipantUserIds(participantUserIds);
        }
        return channel;
    }

    // 사용자의 전체 채널 조회: PUBLIC 채널은 전체 조회, PRIVATE 채널은 해당 사용자가 참여한 채널만 조회한 후 추가 필드 설정
    @Override
    public List<Channel> findAllByUserId(FindAllChannelRequest findAllChannelRequest) {
        List<Channel> publicChannels = channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .collect(Collectors.toList());

        List<ReadStatus> userReadStatuses = readStatusRepository.findByUserId(findAllChannelRequest.userId());
        Set<UUID> privateChannelIds = userReadStatuses.stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());
        List<Channel> privateChannels = channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE && privateChannelIds.contains(channel.getId()))
                .collect(Collectors.toList());

        List<Channel> allChannels = new ArrayList<>();
        allChannels.addAll(publicChannels);
        allChannels.addAll(privateChannels);

        // 각 채널에 대해 추가 필드 설정
        for (Channel channel : allChannels) {
            Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId()).orElse(null);
            channel.setLastMessageTime(lastMessageTime);

            if (channel.getType() == ChannelType.PRIVATE) {
                List<UUID> participantUserIds = readStatusRepository.findByChannelId(channel.getId())
                        .stream()
                        .map(ReadStatus::getUserId)
                        .collect(Collectors.toList());
                channel.setParticipantUserIds(participantUserIds);
            }
        }
        return allChannels;
    }

    // 채널 수정: UpdateChannelRequest DTO를 사용하며, PRIVATE 채널은 수정 불가
    @Override
    public Channel update(UpdateChannelRequest updateChannelRequest) {
        UUID channelId = updateChannelRequest.channelId();
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        channel.updateChannelInfo(updateChannelRequest.newName(), updateChannelRequest.newDescription());
        return channelRepository.save(channel);
    }

    // 채널 삭제: 관련 도메인(Message, ReadStatus)도 함께 삭제
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        // 관련 레포 같이 삭제
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }
}
