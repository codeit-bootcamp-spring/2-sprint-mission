package com.sprint.sprint2.discodeit.service.file;

import com.sprint.sprint1.mission.repository.Repository;
import com.sprint.sprint2.discodeit.entity.Channel;
import com.sprint.sprint2.discodeit.entity.ChannelType;
import com.sprint.sprint2.discodeit.entity.Message;
import com.sprint.sprint2.discodeit.repository.ChannelRepository;
import com.sprint.sprint2.discodeit.repository.file.FileMessageRepository;
import com.sprint.sprint2.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private final FileMessageRepository fileMessageRepository;

    public FileMessageService(FileMessageRepository fileMessageRepository) {
        this.fileMessageRepository = fileMessageRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message message = fileMessageRepository.findById(messageId.toString());
        return message;
    }

    @Override
    public List<Message> findAll() {
        return fileMessageRepository.findByAll().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = fileMessageRepository.findById(messageId.toString());
        message.update(newContent);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        fileMessageRepository.delete(messageId);
    }
}
