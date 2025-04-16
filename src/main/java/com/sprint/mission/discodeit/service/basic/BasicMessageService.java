package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageJPARepository messageJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelJPARepository channelJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final BinaryContentService binaryContentService;
    private final MessageMapper messageMapper;


    @Override
    @Transactional
    public MessageResponseDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtoList) {
        User matchingUser = userJpaRepository.findById(messageCreateDto.authorId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Channel matchingChannel = channelJpaRepository.findById(messageCreateDto.channelId())
                .orElseThrow(() -> new NotFoundException("Channel not found."));

        List<BinaryContent> attachments = binaryContentCreateDtoList.stream()
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentJpaRepository.save(binaryContent);
                })
                .toList();

        Message messages = new Message(messageCreateDto.content(), matchingChannel, matchingUser, attachments);
        messageJpaRepository.save(messages);

        return messageMapper.toDto(messages);
    }


    @Override
    public MessageResponseDto find(UUID messageId) {
        Message matchingMessage =  messageJpaRepository.findByIdEntityGraph(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));
        System.out.println(matchingMessage);
        return messageMapper.toDto(matchingMessage);
    }


    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        List<MessageResponseDto> messageAll = new ArrayList<>();
        messageJpaRepository.findByChannel_IdEntityGraph(channelId).stream()
                .map(messageMapper::toDto)
                .forEach(messageAll::add);
        return messageAll;
    }


    @Override
    @Transactional
    public MessageResponseDto update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.updateMessage(messageUpdateDto.newContent());
        Message updateMessage = messageJpaRepository.save(matchingMessage);
        return messageMapper.toDto(updateMessage);
    }


    @Override
    @Transactional
    public void delete(UUID messageId) {
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.getAttachments().stream()
                .map(BinaryContent::getId)
                .forEach(binaryContentService::delete);

        messageJpaRepository.delete(matchingMessage);

    }

//    MessageResponseDto toDto(Message message){
//        UserResponseDto author = messageJpaRepository.findById(message.getAuthor().getId()).stream()
//                .map(Message::getAuthor)
//                .map(u -> new UserResponseDto(
//                        u.getId(),
//                        u.getUsername(),
//                        u.getEmail(),
//                        u.getProfile() != null ? u.getProfile().getId() : null,
//                        u.getStatus().currentUserStatus()
//                )).toList();
//        return MessageResponseDto.fromMessage(message);


//    }
}
