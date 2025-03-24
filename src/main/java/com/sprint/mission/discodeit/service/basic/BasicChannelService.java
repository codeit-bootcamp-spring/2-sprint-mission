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

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void createPrivateChannel(ChannelCreateDto createDto) {
        User user = userRepository.findById(createDto.userId());
        validatePermission(user);

        Channel channel = createDto.convertDtoToChannel();
        UUID channelId = channelRepository.createChannel(channel);

        channel.getUserMembers().stream()
                .map(channelMemberId -> new ReadStatus(channelMemberId, channelId))
                .forEach(readStatusRepository::createReadStatus);
    }

    @Override
    public void createPublicChannel(ChannelCreateDto createDto) {
        User user = userRepository.findById(createDto.userId());
        validatePermission(user);

        Channel channel = createDto.convertDtoToChannel();
        channelRepository.createChannel(channel);
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        User user = userRepository.findById(userId);
        validatePermission(user);
        validateMembers(userMembers);

        channelRepository.addMembers(id, userMembers, userId);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        User user = userRepository.findById(userId);
        validatePermission(user);
        validateMembers(userMembers);

        channelRepository.removeMembers(id, userMembers, userId);
    }

    @Override
    public ChannelResponseDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id);

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
    public void updateChannel(ChannelUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.userId());
        validatePermission(user);
        Channel channel = channelRepository.findById(updateDto.id());
        if (channel.getType() == ChannelType.PRIVATE){
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channelRepository.updateChannel(updateDto.id(), updateDto.name(), updateDto.category(), ChannelType.PUBLIC, updateDto.userId());
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        User user = userRepository.findById(userId);
        validatePermission(user);

        if (!messageRepository.findAllByChannelId(id).isEmpty()){
            messageRepository.deleteMessageByChannelId(id);
        }
        readStatusRepository.deleteReadStatusByChannelId(id);

        channelRepository.deleteChannel(id, userId);
    }

    /*******************************
     * Validation check
     *******************************/
    private void validatePermission(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("해당 작업은 관리자만 가능합니다.");
        }
    }

    private void validateMembers(Set<UUID> userMembers) {
        for (UUID memberId : userMembers) {
            User user = userRepository.findById(memberId);
            if(user == null){
                throw new NoSuchElementException("해당 ID의 사용자가 존재하지 않습니다: " + memberId);
            }
        }
    }

}
