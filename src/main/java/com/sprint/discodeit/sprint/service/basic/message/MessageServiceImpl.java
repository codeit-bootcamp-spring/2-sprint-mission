package com.sprint.discodeit.sprint.service.basic.message;

import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.domain.mapper.MessageMapper;
import com.sprint.discodeit.sprint.repository.BinaryContentRepository;
import com.sprint.discodeit.sprint.repository.ChannelRepository;
import com.sprint.discodeit.sprint.repository.MessageRepository;
import com.sprint.discodeit.sprint.repository.file.BaseBinaryContentRepository;
import com.sprint.discodeit.sprint.repository.file.FileMessageRepository;
import com.sprint.discodeit.sprint.service.basic.util.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;


    @Override
    public Message create(Long channelId, MessageRequestDto messageRequestDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("없는 채널 입니다."));
        Message message = MessageMapper.toMessage(messageRequestDto);
        message.addChannel(channel);
        List<BinaryContent> binaryContents = binaryContentService.convertToBinaryContents(messageRequestDto.file());
        message.addAllBinaryContents(binaryContents);
        messageRepository.save(message);
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
