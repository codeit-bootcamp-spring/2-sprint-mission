package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/*
@Builder -> 도메인 객체 생성자 자동 생성
@Bean -> Service, RepoConfig 설정을 통해 객체를 컨테이너에 등록
@RequiredArgsConstructor -> 서비스 객체 생성자 자동 생성 (필수 인자 전달 받을 생성자)
- "final 필드"(의존성으로 주입될 필드)를 매개변수로 받는 생성자 자동 생성 (Lombok 제공)
 */

//@Service
@RequiredArgsConstructor
public class BasicChannelServiceImp implements ChannelService {
    private final ChannelRepository channelRepository;

//    public BasicChannelServiceImp(ChannelRepository channelRepository) {
//        this.channelRepository = channelRepository;
//    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
//        Channel channel = new Channel(type, name, description);
        Channel channel = Channel.builder()
                .id(UUID.randomUUID())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .type(type)
                .name(name)
                .description(description)
                .build();
        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                        .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.updateChannelInfo(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }
}
