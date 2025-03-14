package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel newChannel = new Channel("private");      // name과 description 속성은 생략을 어떤식으로 할까???????????
        this.channelRepository.add(newChannel);

        // for 문이 transaction 처리가 간편하다 하여 stream 사용X
        for(UserReadResponse user : privateChannelCreateRequest.users()) {
            this.readStatusRepository.add(new ReadStatus(user.userId(), newChannel.getId()));
        }
        return newChannel;
    }

    @Override
    public Channel createPublicChannel(String channelName) {
        Channel newChannel = new Channel(channelName);      //channelName에 대한 유효성 검증은 Channel 생성자에게 맡긴다.
        this.channelRepository.add(newChannel);
        return newChannel;
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return this.channelRepository.findById(channelId);
    }

    @Override
    public Map<UUID, Channel> readAllChannels() {
        return this.channelRepository.getAll();
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        return this.messageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        this.channelRepository.updateChannelName(channelId, newChannelName);
    }

    @Override
    public void addChannelParticipant(UUID channelId, UUID newParticipantId) {        // channelId 검증은 channelRepository 에서 수행
        UserService.validateUserId(newParticipantId, this.userRepository);
        this.channelRepository.addParticipant(channelId, newParticipantId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        this.channelRepository.deleteById(channelId);
    }
}
