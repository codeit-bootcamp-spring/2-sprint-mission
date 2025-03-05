package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> data = new ArrayList<>();
    private final UserService userService;
    private final ChannelService channelService;
    private static JCFMessageService getInstance;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (getInstance == null) {
            getInstance = new JCFMessageService(userService, channelService);
        }
        return getInstance;
    }

    @Override
    public void sendMessage(UUID channelUUID, UUID userUUID, String content) {

        if (channelService.findChannel(channelUUID) == null) {
            return;
        }

        if (userService.findByUser(userUUID) == null) {
            return;
        }

        Message message = new Message(channelUUID, userUUID, content);
        data.add(message);

        System.out.println("메세지 전송 성공" + message);
    }

    @Override
    public Message findMessageById(UUID messageUUID) {
        return data.stream().filter(message -> message.getId().equals(messageUUID)).findAny().orElse(null);
    }

    @Override
    public Optional<List<Message>> findAllMessages() {
        if (data.isEmpty()) {
            System.out.println("입력 메시지가 존재하지 않습니다.");
            return  Optional.empty();
        }

        return Optional.of(data);
    }

    @Override
    public Optional<List<Message>> findMessageByChannelId(UUID channelUUID) {
        if (channelService.findChannel(channelUUID) == null) {
            return  Optional.empty();
        }

        if (data.stream().noneMatch(message -> message.getChannelUUID().equals(channelUUID))) {
            System.out.println("채널에 해당하는 메시지가 존재하지 않습니다.");
            return Optional.empty();
        }

        return Optional.of(data);
    }

    @Override
    public void updateMessage(UUID id, String content) {
        if (data.stream().noneMatch(data -> data.getId().equals(id))) {
            System.out.println("[실패]수정하려는 메세지가 존재하지 않습니다.");
            return;
        }

        for (Message message : data) {
            if (message.getId().equals(id)) {
                message.setContent(content);
                message.setUpdatedAt(System.currentTimeMillis());
                System.out.println("[성공]메시지 변경 완료[메시지 아이디: " + message.getId() +
                        ", 변경 시간: " + message.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void deleteMessageById(UUID id) {
        boolean isremove = data.removeIf(message -> message.getId().equals(id));

        if (!isremove) {
            System.out.println("[실패]삭제하려는 메시지가 존재하지 않습니다.");
        } else {
            System.out.println("[성공]메세지 삭제 완료");
        }
    }

}
