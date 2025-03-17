package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository; // 추가
import com.sprint.mission.discodeit.repository.ReadStatusRepository; // 추가
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequestDTO; // 추가
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequestDTO; // 추가
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Primary
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository; // 추가
    private final ReadStatusRepository readStatusRepository; // 추가

    public ChannelServiceImpl(ChannelRepository channelRepository, MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository; // 추가
        this.readStatusRepository = readStatusRepository; // 추가
    }

    public Channel createPublicChannel(PublicChannelCreateRequestDTO dto) {
        Channel channel = new Channel(ChannelType.PUBLIC, dto.getName(), dto.getDescription());
        return channelRepository.save(channel);
    }

    public Channel createPrivateChannel(PrivateChannelCreateRequestDTO dto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null); // PRIVATE 채널은 name, description 생략
        Channel savedChannel = channelRepository.save(channel);

        // User 별 ReadStatus 생성
        dto.getUserIds().forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(userId, savedChannel.getId(), false);
            readStatusRepository.save(readStatus);
        });

        return savedChannel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = find(channelId);
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다."); // 수정 불가 예외 처리
        }
        channel.setName(newName);
        channel.setDescription(newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        // 해당 채널의 Message, ReadStatus 데이터 삭제 로직 추가
        messageRepository.deleteByChannelId(channelId); // 추가
        readStatusRepository.deleteByChannelId(channelId); // 추가
        channelRepository.deleteById(channelId);
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        if (type == ChannelType.PUBLIC) {
            return createPublicChannel(new PublicChannelCreateRequestDTO(name, description));
        } else {
            return createPrivateChannel(new PrivateChannelCreateRequestDTO()); // PRIVATE 채널은 name, description 생략
        }
    }
}