package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageReqDto;
import com.sprint.mission.discodeit.dto.message.MessageResDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageReqDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResDto create(CreateMessageReqDto createMessageReqDto) {
        userRepository.findById(createMessageReqDto.userId()).orElseThrow(() -> new NoSuchElementException("해당 user 객체를 찾을 수 없습니다."));
        channelRepository.findById(createMessageReqDto.channelId()).orElseThrow(() -> new NoSuchElementException("해당 channel 객체를 찾을 수 없습니다."));

        List<UUID> attachmentsId = new ArrayList<>();

        if(createMessageReqDto.fileDatas() != null) {
            for (byte[] fileData : createMessageReqDto.fileDatas()) {
                if(fileData != null && fileData.length > 0) {
                    BinaryContent binaryContent = new BinaryContent(fileData);
                    binaryContentRepository.save(binaryContent);
                    attachmentsId.add(binaryContent.getId());
                }
            }
        }

        Message message = new Message(createMessageReqDto.userId(), createMessageReqDto.channelId(), createMessageReqDto.content(), attachmentsId);
        messageRepository.save(message);
        return new MessageResDto(message.getId(), message.getId(), message.getChannelId(), message.getContent(), message.getAttachmentsId());
    }

    @Override
    public MessageResDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        return new MessageResDto(message.getId(), message.getId(), message.getChannelId(), message.getContent(), message.getAttachmentsId());
    }

    @Override
    public List<MessageResDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(message -> new MessageResDto(message.getId(), message.getId(), message.getChannelId(), message.getContent(), message.getAttachmentsId()))
                .toList();
    }

    @Override
    public void update(UUID messageId, UpdateMessageReqDto updateMessageReqDto) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        List<UUID> attachmentsId = new ArrayList<>();

        if(updateMessageReqDto.fileDatas() != null) {
            for (byte[] b : updateMessageReqDto.fileDatas()) {
                if(b != null && b.length > 0) {
                    BinaryContent binaryContent = new BinaryContent(b);
                    binaryContentRepository.save(binaryContent);
                    attachmentsId.add(binaryContent.getId());
                }
            }
        }

        message.updateMessage(updateMessageReqDto.content(), attachmentsId, Instant.now());
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        //연관된 데이터 삭제
        for (UUID binaryContentId : message.getAttachmentsId()) {
            binaryContentRepository.deleteById(binaryContentId);
        }

        messageRepository.deleteById(message.getId());
    }
}
