package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.FindChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public void createPublicChannel(String channelName, ChannelType channelType) {
        Channel channel = channelRepository.save(channelName, channelType);
        if (channel == null) {
            System.out.println("[실패] 채널 저장 실패");
            return;
        }
        System.out.println("[성공]" + channel);
    }

    @Override
    public void createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList) {
        Channel channel = channelRepository.save(channelName, channelType);
        if (channel == null) {
            System.out.println("[실패] 채널 저장 실패");
            return;
        }
        userList.forEach(userUUID -> readStatusRepository.save(userUUID, channel.getId()));
        System.out.println("[성공]" + channel);
    }

    @Override
    public FindChannelDto findChannel(UUID channelUUID) {
        Channel channel = channelRepository.findChannelById(channelUUID)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        List<Message> messageList = messageRepository.findMessageByChannel(channelUUID);

        Message lastMessage = messageList.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElseThrow(() -> new IllegalArgumentException("메세지를 찾을 수 없습니다."));

        if(channel.getChannelType().equals(ChannelType.PRIVATE)) {
            return new FindChannelDto(channelUUID, channel.getChannelName(), lastMessage.getCreatedAt());
        }

        return new FindChannelDto(channelUUID, channel.getChannelName(), lastMessage.getUserUUID(),lastMessage.getCreatedAt());
    }

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = channelRepository.findAllChannel();
        if (channelList.isEmpty()) {
            System.out.println("채널이 존재하지 않습니다.");
        }
        return channelList;
    }

    @Override
    public void updateChannel(UUID channelUUID, String channelName) {
        Channel channel = channelRepository.updateChannelChannelName(channelUUID, channelName);
        if (channel == null) {
            System.out.println("[실패] 채널 변경 실패");
            return;
        }
        System.out.println("[성공]" + channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        boolean isDeleted = channelRepository.deleteChannelById(id);
        if (!isDeleted) {
            System.out.println("[실패] 채널 삭제 실패");
            return;
        }
        System.out.println("[성공] 채널 삭제 완료");
    }
}
