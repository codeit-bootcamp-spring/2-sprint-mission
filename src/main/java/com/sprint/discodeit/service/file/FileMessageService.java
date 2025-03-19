package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.domain.entity.Message;
import com.sprint.discodeit.domain.mapper.MessageMapper;
import com.sprint.discodeit.repository.file.FileMessageRepository;
import com.sprint.discodeit.service.MessageServiceV1;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMessageService implements MessageServiceV1 {

    private final FileMessageRepository fileMessageRepository;
    private final BinaryContentService binaryContentService;

    @Override
    public Message create(MessageRequestDto messageRequestDto) {
        List<BinaryContent> binaryContents = binaryContentService.convertToBinaryContents(messageRequestDto.file());
        binaryContentService.saveBinaryContents(binaryContents);
        List<UUID> uuids = binaryContentService.convertToUUIDs(binaryContents);
        Message message = MessageMapper.toMessage(messageRequestDto, uuids);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message message = fileMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));;
        return message;
    }

    @Override
    public List<Message> findAll(UUID channelId) {
        return fileMessageRepository.findByChannelAndMessageContext(channelId)
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
