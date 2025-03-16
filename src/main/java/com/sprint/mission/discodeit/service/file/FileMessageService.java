package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Exception.EmptyMessageListException;
import com.sprint.mission.discodeit.Repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.Repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileMessageService implements MessageService {
    private final FileUserRepository userRepository;
    private final FileChannelRepository serverRepository;
    private final FileMessageRepository channelRepository;


    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            channelRepository.reset();
        }
    }

    @Override
    public Message write(String creatorId, String channelId, String text) {
        UUID UID = UUID.fromString(creatorId);
        UUID CID = UUID.fromString(channelId);
        System.out.println("🔍 write: 요청된 creatorId: " + creatorId);
        System.out.println("🔍 write: 요청된 channelId: " + channelId);

        User user = userRepository.findUserByUserId(UID);
        System.out.println("🔍 write: 반환된 user: " + user.getId());

        Message message = new Message(UID,user.getName(), CID, text);

        channelRepository.save(message);
        return message;
    }

    @Override
    public Message getMessage(String serverId, String channelId, String messageId) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);

        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.find(channel, MID);

        return message;
    }

    @Override
    public void printMessage(String serverId, String channelId) {
        try {
            UUID SID = UUID.fromString(serverId);
            UUID CID = UUID.fromString(channelId);

            Channel channel = serverRepository.findChannelByChanelId(SID, CID);
            System.out.println(channel.getName());

            List<Message> messages = channelRepository.findAllByChannelId(channel);
            for (Message message : messages) {
                System.out.println(message.getCreatorName() + " : " + message.getText());
            }
        } catch (EmptyMessageListException e) {
            System.out.println("메시지 함이 비어있습니다.");
        }

    }

    @Override
    public boolean removeMessage(String serverId,String channelId, String messageId) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);
        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.find(channel, MID);

        channelRepository.remove(channel, message);
        return true;
    }

    @Override
    public boolean updateMessage(String serverId,String channelId, String messageId, String replaceText) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);
        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.find(channel, MID);

        channelRepository.update(channel, message, replaceText);
        return true;
    }
}
