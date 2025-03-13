package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private static BasicMessageService instance;
    private final MessageRepository messageRepository;
    private final FileUserService userservice;
    private final FileChannelService channelservice;

    private BasicMessageService(MessageRepository messageRepository, FileUserService userservice, FileChannelService channelservice) {
        this.messageRepository = messageRepository;
        this.userservice = userservice;
        this.channelservice = channelservice;
    }

    public static synchronized BasicMessageService getInstance(MessageRepository messageRepository, FileUserService userservice, FileChannelService channelservice) {
        if (instance == null) {
            instance = new BasicMessageService(messageRepository, userservice, channelservice);
        }
        return instance;
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId) {
        if (!userservice.existUser(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");
        }
        if (!channelservice.existChannel(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        Message message = new Message(userId, channelId);
        messageRepository.save(message);
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message.getId();
    }

    @Override
    public void searchMessage(UUID id) {
        Message message = messageRepository.findById(id);
        if (message == null) {
            System.out.println("조회하신 메세지가 존재하지 않습니다.");
            return;
        }
        System.out.println("MESSAGE: " + message);
    }

    @Override
    public void searchAllMessages() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            System.out.println("등록된 메세지가 없습니다.");
            return;
        }
        messages.forEach(message -> System.out.println("MESSAGE: " + message));
    }

    @Override
    public void updateMessage(UUID id) {
        Message message = messageRepository.findById(id);
        if (message == null) {
            System.out.println("업데이트할 메세지가 존재하지 않습니다.");
            return;
        }
        message.updateTime(System.currentTimeMillis());
        messageRepository.update(message);
        System.out.println(id + " 메세지 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteMessage(UUID id) {
        if (!messageRepository.existsById(id)) {
            System.out.println("삭제할 메세지가 존재하지 않습니다.");
            return;
        }
        messageRepository.delete(id);
        System.out.println(id + " 메세지 삭제 완료되었습니다.");
    }

    public boolean existMessage(UUID id) {
        return messageRepository.existsById(id);
    }
}
