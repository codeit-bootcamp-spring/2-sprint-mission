package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final List<Message> data;

    private JCFMessageService() {
        data = new ArrayList<>();
    }

    // 싱글톤 패턴 적용 - 지금은 javaApplication에서만 사용되는데 굳이 적용할 필요가 있을까? -> 나중을 위해 적용해놓자.
    // 아직까진 멀티 쓰레드 환경이 아니므로 게으른 초기화 사용
    private static MessageService messageService;

    public static MessageService getInstance() {
        if(messageService == null) {
            messageService = new JCFMessageService();
        }
        return messageService;
    }
    // 싱글톤이나 생성자 주입을 사용하지 않는다면, 뭔가를 할 때마다 Service 객체가 새로 생성 -> 조회하거나 할 때 서로 다른 객체의 필드에 들어있어 못찾을수도 있음


    // 메시지 생성
    @Override
    public void createMessage(Message message) {

        // 해당 채널의 유저인지 확인
        if(!message.getChannel().getUsers().contains(message.getSender())) {
            throw new IllegalStateException("해당 채널의 유저가 아닙니다.");
        }
        if (message == null) {
            throw new IllegalArgumentException("생성하려는 메시지 정보가 존재하지 않습니다.");
        }
        if (message.getChannel() == null || message.getContent() == null || message.getSender() == null) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }

        data.add(message);
        // 메시지를 생성하면 User의 Message 목록에도 추가 (양방향 유지)
        message.getSender().addMessage(message);
        // 메시지를 생성하면 Channel의 Message 목록에도 추가 (양방향 유지)
        message.getChannel().addMessage(message);
    }

    // 메시지 단건 조회
    @Override
    public Message getMessage(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("검색하려는 메시지의 Id값을 입력하지 않았습니다.");
        }
        Message findMessage = data.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 Id값을 가진 메시지는 존재하지 않습니다."));

        return findMessage;
    }

    // 메시지 전체 조회
    @Override
    public List<Message> getAllMessages() {
        return data;
    }

    // 메시지 수정
    @Override
    public void updateMessage(UUID id, User owner, String content) { // 수정정보가 담겨있는 message 객체는 new로 새로 생성되므로 UUID값이 다름 -> 파라미터로 수정하려는 메시지의 UUID도 받자!
        if (content == null) {
            throw new IllegalArgumentException("메시지 수정 정보가 존재하지 않습니다.");
        }
        Message findMessage = getMessage(id);

        // 메시지의 주인이 아니면 Exception 발생
        if (!owner.equals(findMessage.getSender())) {
            throw new IllegalStateException("메시지 작성자만 수정할 수 있습니다.");
        }

        findMessage.updateUpdatedAt(System.currentTimeMillis());
        findMessage.updateContent(content);
    }

    // 메시지 삭제
    @Override
    public void deleteMessage(UUID id, User owner) {
        Message findMessage = getMessage(id);

        // 채널 관리자나 메시지의 주인이 아니면 Exception 발생
        if (!owner.equals(findMessage.getChannel().getOwner()) && !owner.equals(findMessage.getSender())) {
            throw new IllegalStateException("메시지 작성자나 관리자만 삭제할 수 있습니다.");
        }

        // 메시지 삭제 시 사용자의 messages 필드에도 삭제되게끔!
        findMessage.getSender().getMessages().remove(findMessage);

        data.remove(findMessage);

    }
    // 해당 채널의 메시지 전체 조회
    @Override
    public List<Message> getAllMessagesByChannel(String channelName) {
        if(channelName == null) {
            throw new IllegalArgumentException("검색에 필요한 정보가 입력되지 않았습니다. (채널명 필요)");
        }
        List<Message> findMessages = getAllMessages();

        return findMessages.stream().filter(m -> m.getChannel().getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    // 해당 채널의 해당 유저의 메시지만 검색
    @Override
    public List<Message> searchMessageByChannelAndUser(String channelName, String username) {
        if(channelName == null) {
            throw new IllegalArgumentException("검색에 필요한 정보가 입력되지 않았습니다. (채널명 필요)");
        }
        List<Message> messages = getAllMessagesByChannel(channelName);

        // username이 null이나 빈칸일 경우 그냥 채널 메시지의 전체 리스트 반환
        if(username == null || username.isBlank()) {
            return messages;
        }

        return messages.stream()
                .filter(m -> m.getSender().getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // 해당 채널의 메시지를 포함 내용으로 검색
    @Override
    public List<Message> searchMessagesContaining(String channelName, String content) {
        if(channelName == null) {
            throw new IllegalArgumentException("검색에 필요한 정보가 입력되지 않았습니다. (채널명 필요)");
        }

        // 이 메소드에서 channelName에 대한 예외처리를 해주므로, 따로 필요하지 않음
        List<Message> messages = getAllMessagesByChannel(channelName);

        // content가 null이나 빈칸일 경우 그냥 채널 메시지의 전체 리스트 반환 (검색어가 없으므로)
        if(content == null || content.isBlank()) {
            return messages;
        }

        return messages.stream()
                .filter(m -> m.getContent().contains(content))
                .collect(Collectors.toList());
    }
}
