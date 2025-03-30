package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.CheckReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.FindReadStatusByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.SaveReadStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusParamDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Override
    public void save(SaveReadStatusParamDto saveReadStatusParamDto) {
        User user = userRepository.findUserById(saveReadStatusParamDto.userId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepository.findChannelById(saveReadStatusParamDto.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        ReadStatus readStatus = ReadStatus.builder()
                .channelId(channel.getId())
                .userId(user.getId())
                .build();

        readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID readStatusUUID) {
        return readStatusRepository.find(readStatusUUID)
                .orElseThrow(() -> new NoSuchElementException("읽은 상태를 찾을 수 없습니다"));
    }

    @Override
    public List<CheckReadStatusResponseDto> findAllByUserId(FindReadStatusByUserIdRequestDto findReadStatusByUserIdRequestDto) {
        return readStatusRepository.findByUserId(findReadStatusByUserIdRequestDto.userUUID()).stream()
                .flatMap(readStatus -> channelRepository.findChannelById(readStatus.getChannelId())
                        .flatMap(channel -> messageRepository.findMessageByChannel(channel.getId()).stream()
                                .max(Comparator.comparing(Message::getCreatedAt))
                                .map(lastMessage -> new CheckReadStatusResponseDto(
                                        channel.getId(),
                                        channel.getChannelName(),
                                        lastMessage.getCreatedAt(),
                                        readStatus.isRead(lastMessage.getCreatedAt())
                                )))
                        .stream())
                .collect(Collectors.toList());
    }

    @Override
    public void update(UpdateReadStatusParamDto updateReadStatusParamDto) {
        User user = userRepository.findUserById(updateReadStatusParamDto.userUUID())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepository.findChannelById(updateReadStatusParamDto.channelUUID())
                .filter(data -> data.getChannelType().equals(ChannelType.PRIVATE))
                .orElseThrow(() -> new NoSuchElementException("비공개 채널을 찾을 수 없습니다."));

        ReadStatus readStatus = readStatusRepository.findByChannelId(channel.getId())
                .stream().findAny()
                .filter(data -> data.getUserId().equals(user.getId()))
                .map(data -> {
                    data.updateLastReadAt();
                    return data;
                })
                .orElseThrow(() -> new IllegalArgumentException("상태 업데이트 중 오류 발생"));

        readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusUUID) {
        readStatusRepository.delete(readStatusUUID);
    }
}
