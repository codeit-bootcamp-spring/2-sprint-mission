package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.ChannelCreateDto;
import com.sprint.mission.discodeit.service.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPrivateChannel(ChannelCreateDto createDto) {
        User user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + createDto.userId()));;
        validatePermission(user);

        Channel channel = channelRepository.save(createDto.convertDtoToChannel());

        channel.getUserMembers().stream()
                .map(channelMemberId -> new ReadStatus(channelMemberId, channel.getId()))
                .forEach(readStatusRepository::save);

        return channel;
    }

    @Override
    public Channel createPublicChannel(ChannelCreateDto createDto) {
        User user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + createDto.userId()));;
        validatePermission(user);

        return channelRepository.save(createDto.convertDtoToChannel());
    }

    @Override
    public ChannelResponseDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id));

        Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(id).orElse(null);
        Set<UUID> channelMembers = channel.getType() == ChannelType.PRIVATE ? channel.getUserMembers() : null;

        return ChannelResponseDto.convertToResponseDto(channel, channelMembers, lastMessageCreatedAt);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannelsList = channelRepository.findByType(ChannelType.PUBLIC);
        List<Channel> privateChannelsList = channelRepository.findByUserIdAndType(userId, ChannelType.PRIVATE);
        List<Channel> channelsList = Stream.concat(publicChannelsList.stream(), privateChannelsList.stream())
                .toList();

        List<ChannelResponseDto> resultList = new ArrayList<>();
        channelsList.stream()
                .map(channel -> {
                    Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(channel.getId()).orElse(null);
                    Set<UUID> channelMembers = channel.getType() == ChannelType.PRIVATE ? channel.getUserMembers() : null;
                    return ChannelResponseDto.convertToResponseDto(channel, channelMembers, lastMessageCreatedAt);
                })
                .forEach(resultList::add);

        return resultList;
    }

    @Override
    public Channel updateChannel(ChannelUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + updateDto.userId()));;
        validatePermission(user);

        Channel channel = channelRepository.findById(updateDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + updateDto.id()));
        if (channel.getType() == ChannelType.PRIVATE){
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(updateDto.name(), updateDto.category(), updateDto.type());
        return channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));;
        validatePermission(user);

        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id));

        if (!messageRepository.findAllByChannelId(channel.getId()).isEmpty()){
            messageRepository.deleteAllByChannelId(id);
        }
        if (findChannelById(channel.getId()).type() == ChannelType.PRIVATE){
            readStatusRepository.deleteAllByChannelId(id);
        }

        channelRepository.deleteById(id);
    }

//    @Override
//    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
//        User user = userRepository.findById(userId);
//        validatePermission(user);
//        validateMembers(userMembers);
//
//        channelRepository.addMembers(id, userMembers, userId);
//    }
//
//    @Override
//    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
//        User user = userRepository.findById(userId);
//        validatePermission(user);
//        validateMembers(userMembers);
//
//        channelRepository.removeMembers(id, userMembers, userId);
//    }

    /*******************************
     * Validation check
     *******************************/
    private void validatePermission(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalStateException("해당 작업은 관리자만 가능합니다.");
        }
    }

//    private void validateMembers(Set<UUID> userMembers) {
//        for (UUID memberId : userMembers) {
//            User user = userRepository.findById(memberId)
//                    .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + memberId));;
//        }
//    }


}
