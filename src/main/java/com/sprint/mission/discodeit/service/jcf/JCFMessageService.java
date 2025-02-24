package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    // 메시지 생성
    @Override
    public void createMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("생성하려는 메시지 정보가 올바르지 않습니다.");
        }
        if (message.getChannel() == null || message.getContent().isEmpty() || message.getSender() == null) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }

        data.add(message);
    }

    // 메시지 단건 조회
    @Override
    public Message getMessage(UUID id) {
        Message findMessage = data.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));

        return findMessage;
    }

    // 메시지 전체 조회
    @Override
    public List<Message> getAllMessages() {
        return data;
    }

    // 메시지 수정
    @Override
    public void updateMessage(UUID id, User owner, Message message) { // 수정정보가 담겨있는 message 객체는 new로 새로 생성되므로 UUID값이 다름 -> 파라미터로 수정하려는 메시지의 UUID도 받자!
        if (message == null) {
            throw new IllegalArgumentException("수정 정보가 존재하지 않습니다.");
        }
        Message findMessage = getMessage(id);

        // 필수 입력 항목이 하나라도 빠지면 Exception 발생
        if (message.getContent() == null) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }

        // 메시지의 주인이 아니면 Exception 발생
        if (!owner.equals(findMessage.getSender())) {
            throw new IllegalArgumentException("메시지 작성자만 수정할 수 있습니다.");
        }

        findMessage.updateUpdatedAt(System.currentTimeMillis());
        findMessage.updateContent(message.getContent());
    }

    // 메시지 삭제
    @Override
    public void deleteMessage(UUID id, User owner) {
        Message findMessage = getMessage(id);

        // 채널 관리자나 메시지의 주인이 아니면 Exception 발생
        if (!owner.equals(findMessage.getChannel().getOwner()) && !owner.equals(findMessage.getSender())) {
            throw new IllegalArgumentException("메시지 작성자나 관리자만 삭제할 수 있습니다.");
        }

        data.remove(findMessage);

    }
}
