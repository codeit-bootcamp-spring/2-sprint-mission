package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void sendMessage(SaveMessageParamDto saveMessageParamDto) {
        if (userService.findByUser(saveMessageParamDto.UserId()) == null) {
            return;
        }

        if (channelService.findChannel(saveMessageParamDto.channelId()) == null) {
            return;
        }

        List<UUID> attachmentList = saveMessageParamDto.imageList().stream()
                .map(image -> binaryContentRepository.save(image).getId())
                .toList();

        Message message = messageRepository.save(saveMessageParamDto.channelId(), saveMessageParamDto.UserId(), saveMessageParamDto.content(), attachmentList);
        if (message == null) {
            System.out.println("[실패] 메세지 저장 실패");
            return;
        }
        System.out.println("[성공]" + saveMessageParamDto.toString());
    }

    @Override
    public Message findMessageById(UUID messageUUID) {
        return messageRepository.findMessageById(messageUUID)
                .orElseGet(() -> {
                    System.out.println("메세지가 존재하지 않습니다.");
                    return null;
                });
    }

    @Override
    public List<Message> findAllMessages() {
        List<Message> messageList = messageRepository.findAllMessage();
        if (messageList.isEmpty()) {
            System.out.println("메세지가 존재하지 않습니다.");
        }
        return messageList;
    }

    @Override
    public List<Message> findMessageByChannelId(UUID channelUUID) {
        List<Message> channelMessageList = messageRepository.findMessageByChannel(channelUUID);
        if (channelMessageList.isEmpty()) {
            System.out.println("해당 채널에 메세지가 존재하지 않습니다");
        }
        return channelMessageList;
    }

    @Override
    public void updateMessage(UpdateMessageParamDto updateMessageParamDto) {
        Message message = messageRepository.updateMessage(updateMessageParamDto.messageUUID(), updateMessageParamDto.content());
        if (message == null) {
            System.out.println("[실패] 메세지 수정 실패");
        }
    }

    @Override
    public void deleteMessageById(UUID messageUUID) {
        Message message = messageRepository.findMessageById(messageUUID)
                        .orElseThrow(NullPointerException::new);
        message.getAttachmentList().forEach(binaryContentRepository::delete);
        boolean isDelete = messageRepository.deleteMessageById(message.getId());
        if (!isDelete) {
            System.out.println("[실패] 메세지 삭제 실패");
            return;
        }
        System.out.println("[성공] 메세지 삭제 완료");
    }
}
