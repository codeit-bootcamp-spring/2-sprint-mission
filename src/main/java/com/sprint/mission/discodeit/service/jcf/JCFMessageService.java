package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData;
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.messageData = new HashMap<>();
        this.channelService = channelService;
        this.userService = userService;
        // channelService와 userService 객체의 메소드를 접근해서 사용하겠다
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String messageContent) {
        if (messageContent.length() < 2) {
            System.out.println("메세지는 두 글자 이상이어야 합니다.");
            return null;
        }
        Message message = new Message(channelId, userId, messageContent);
        messageData.put(message.getId(), message);
        System.out.println("메세지 생성 완료 : " + message);
        return message; //생성된 메세지 반환
    }

    @Override
    public void getMessageInfo(UUID channelId) {
        // 특정 톡방 메시지 출력
        if (channelService.getChannelById(channelId) == null) {
            System.out.println("해당 톡방이 존재하지 않습니다.");
        }

        List<String> messageList = channelMessageList(channelId);

        if (messageList.isEmpty()) {
            System.out.println("해당 채널에 등록된 메시지가 없습니다.");
        } else {
            System.out.println("톡방 메세지 리스트 : " + messageList);
        }
    }

    @Override
    public void getAllMessageData() {
        if (messageData.isEmpty()) {
            System.out.println("메세지 데이터가 없습니다.");
            return;
        }
        System.out.println("모든 메세지 정보 : " + messageData);
    }

    @Override
    public void updateMessage(UUID messageId, String newMessageContent) {
        if (messageData.get(messageId) == null) {
            System.out.println("업데이트 할 메세지를 다시 선택해주세요.");
            return;
        }
        messageData.get(messageId).setMessageContent(newMessageContent);
        System.out.println("메세지가 수정됐습니다. -> " + messageData.get(messageId));
    }

    // 특정 유저의 최근 메세지 삭제
    @Override
    public void deleteMessage(UUID messageId) {
        if (messageData.get(messageId) == null) {
            System.out.println("삭제할 메세지를 다시 선택해 주세요.");
            return;
        }
        messageData.remove(messageId);
        System.out.println("메세지가 삭제 됐습니다.");
    }

    // 특정 채널의 메시지만 리스트로 생성
    private List<String> channelMessageList(UUID channelId) {
        return messageData.values().stream()
                .filter(list -> list.getChannelId().equals(channelId))
                .map(Message::getMessageContent) // 메시지 내용만 추출
                .collect(Collectors.toList()); //최종 연산
    }
}
