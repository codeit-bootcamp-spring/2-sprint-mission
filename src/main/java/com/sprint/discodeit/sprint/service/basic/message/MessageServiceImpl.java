package com.sprint.discodeit.sprint.service.basic.message;

import com.sprint.discodeit.sprint.domain.dto.PaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelMessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.domain.mapper.MessageMapper;
import com.sprint.discodeit.sprint.domain.mapper.PaginationMapper;
import com.sprint.discodeit.sprint.repository.ChannelRepository;
import com.sprint.discodeit.sprint.repository.MessageRepository;
import com.sprint.discodeit.sprint.repository.UsersRepository;
import com.sprint.discodeit.sprint.service.basic.util.BinaryContentService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final ChannelRepository channelRepository;
    private final UsersRepository usersRepository;

    @Override
    public Message create(Long channelId, MessageRequestDto messageRequestDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("없는 채널 입니다."));
        Users users = usersRepository.findById(messageRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원 입니다."));
        Message message = MessageMapper.toMessage(messageRequestDto);

        message.addChannel(channel);
        message.addUsers(users);

        for(String file : messageRequestDto.file()) {
            BinaryContent binaryContent = binaryContentService.createProfileImage(file);
            message.addBinaryContent(binaryContent);
        }
        messageRepository.save(message);
        return message;
    }


    @Override
    public PaginatedResponse<ChannelMessageResponseDto> findChannel(Long channelId, Pageable page) {
        Slice<Message> slice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId,page);
        if(slice.isEmpty()){
            throw new NoSuchElementException(channelId + " 없는 채널 입니다");
        }
        return PaginationMapper.fromSlice(
                slice.map(message -> new ChannelMessageResponseDto(
                        message.getId(),
                        message.getContent(),
                        message.getUsers().getUsername(),
                        message.getCreatedAt()
                ))
        );
    }


    @Override
    public Message update(Long messageId, MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));
        message.update(messageUpdateRequestDto.newContent());
        return message;
    }

    @Override
    public void delete(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new IllegalArgumentException("없는 메세지 입니다."));
        messageRepository.delete(message);
    }
}
