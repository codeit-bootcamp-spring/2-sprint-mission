package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList = new ArrayList<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void sendMessage(SaveMessageParamDto saveMessageParamDto) {
        //if (channelService.findChannel(channelUUID) == null) {
        //    return;
        //}
        //
        //if (userService.findByUser(userUUID) == null) {
        //    return;
        //}
        //
        //Message message = new Message(content, userUUID, channelUUID);
        //messageList.add(message);
        //
        //System.out.println("메세지 전송 성공" + message);
    }

    @Override
    public Message findMessageById(UUID messageUUID) {
        return messageList.stream().filter(message -> message.getId().equals(messageUUID)).findAny().orElse(null);
    }

    @Override
    public List<Message> findAllMessages() {
        if (messageList.isEmpty()) {
            System.out.println("입력 메시지가 존재하지 않습니다.");
        }
        return messageList;
    }

    @Override
    public List<Message> findMessageByChannelId(UUID channelUUID) {
        if (messageList.stream().noneMatch(message -> message.getChannelUUID().equals(channelUUID))) {
            System.out.println("채널에 해당하는 메시지가 존재하지 않습니다.");
        }
        return messageList.stream().filter(message -> message.getChannelUUID().equals(channelUUID)).collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UpdateMessageParamDto updateMessageParamDto) {
        if (messageList.stream().noneMatch(data -> data.getId().equals(updateMessageParamDto.messageUUID()))) {
            System.out.println("[실패]수정하려는 메세지가 존재하지 않습니다.");
            return;
        }

        for (Message message : messageList) {
            if (message.getId().equals(updateMessageParamDto.messageUUID())) {
                message.updateContent(updateMessageParamDto.content());
                System.out.println("[성공]메시지 변경 완료[메시지 아이디: " + message.getId() +
                        ", 변경 시간: " + message.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void deleteMessageById(UUID id) {
        boolean isremove = messageList.removeIf(message -> message.getId().equals(id));

        if (!isremove) {
            System.out.println("[실패]삭제하려는 메시지가 존재하지 않습니다.");
        } else {
            System.out.println("[성공]메세지 삭제 완료");
        }
    }

}
