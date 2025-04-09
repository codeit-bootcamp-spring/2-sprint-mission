package com.sprint.discodeit.sprint5.service.basic.message;

import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint5.domain.entity.Message;
import com.sprint.discodeit.sprint5.domain.mapper.MessageMapper;
import com.sprint.discodeit.sprint5.repository.file.BaseBinaryContentRepository;
import com.sprint.discodeit.sprint5.repository.file.FileMessageRepository;
import com.sprint.discodeit.sprint5.service.basic.util.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageServiceV1 {

    private final FileMessageRepository fileMessageRepository;
    private final BinaryContentService binaryContentService;
    private final BaseBinaryContentRepository baseBinaryContentRepository;


    @Override
    public Message create(UUID channelId, MessageRequestDto messageRequestDto) {
        List<BinaryContent> binaryContents = binaryContentService.convertToBinaryContents(messageRequestDto.file());
        binaryContentService.saveBinaryContents(binaryContents);
        List<UUID> uuids = binaryContentService.convertToUUIDs(binaryContents);
        Message message = MessageMapper.toMessage(messageRequestDto, uuids, channelId);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message message = fileMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));
        return message;
    }

    public List<Message> findChannel(UUID channelId) {
        List<Message> message = fileMessageRepository.findByChannelId(channelId).orElseThrow(() -> new NoSuchElementException(channelId + " 없는 채널 입니다"));
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> message = fileMessageRepository.findByChannelAndMessageContext(channelId);
        if (message.isEmpty() || message.size() == 0) {
          throw new IllegalArgumentException("유효하지 않는 채널 입니다.");
        }
        return message;
    }

    @Override
    public Message update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = fileMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));
        message.update(messageUpdateRequestDto.newContent());
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Optional<Message> message = fileMessageRepository.findById(messageId);
        if(message.isPresent()){
            for(UUID binaryContentsId : message.get().getAttachmentIds()){
                baseBinaryContentRepository.delete(binaryContentsId);
            }
            fileMessageRepository.delete(messageId);
        }else{
            throw new IllegalArgumentException("없는 메세지 입니다.");
        }
    }
}
