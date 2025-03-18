package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.Message;
import com.sprint.discodeit.repository.file.FileMessageRepository;
import com.sprint.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMessageService implements MessageService {

    private final FileMessageRepository fileMessageRepository;

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message message = fileMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));;
        return message;
    }

    @Override
    public List<Message> findAll() {
        return fileMessageRepository.findByAll().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = fileMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));;
        message.update(newContent);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        fileMessageRepository.delete(messageId);
    }
}
