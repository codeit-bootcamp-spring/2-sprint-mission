package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository repository;
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(String filename, ChannelService channelService, UserService userService) {
        this.repository = new FileMessageRepository(filename);
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            channelService.findById(channelId);
            userService.findById(authorId);
        } catch (NoSuchElementException e) {
            System.err.println("🚨 메시지를 보낼 채널 또는 사용자가 존재하지 않습니다.");
            throw e;
        }

        Message message = new Message(content, channelId, authorId);
        repository.save(message);
        System.out.println("메시지 생성 및 저장 완료: " + message.getId());
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        System.out.println("메시지 조회: " + messageId);
        return repository.findById(messageId);
    }

    @Override
    public List<Message> findAll() {
        System.out.println("저장된 모든 메시지 조회");
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
        System.out.println("메시지 삭제 및 저장 완료: " + messageId);
    }
}
