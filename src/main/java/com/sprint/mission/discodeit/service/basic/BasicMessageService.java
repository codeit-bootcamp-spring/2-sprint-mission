package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository repository;
    private final ChannelService channelService;
    private final UserService userService;

    public BasicMessageService(MessageRepository repository, ChannelService channelService, UserService userService) {
        this.repository = repository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        channelService.findById(channelId);
        userService.findById(authorId);
        Message message = new Message(content, channelId, authorId);
        repository.save(message);
        System.out.println("메시지 생성 완료: " + message.getId());
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        System.out.println("메시지 조회: " + messageId);
        return repository.findById(messageId);
    }

    @Override
    public List<Message> findAll() {
        System.out.println("모든 메시지 조회");
        return repository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = repository.findById(messageId);
        message.update(newContent);
        repository.save(message);
        System.out.println("메시지 내용 변경 완료: " + newContent);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        repository.delete(messageId);
        System.out.println("메시지 삭제 완료: " + messageId);
    }
}
