package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.DataConflictException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.mapping.UserMapping;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
@Service
@Validated
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;



    @Override
    public UserDto.Summary findByUserId(UUID id) {
        User user = userRepository.findByUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", id));
        return UserMapping.INSTANCE.userToSummary(user, userStatus);
    }

    @Override
    public List<UserDto.Summary> findByAllUsersId() {

        Set<UUID> userIds = userRepository.findAllUsers();
        List<UserDto.Summary> summaryList = new ArrayList<>();

        for (UUID userId : userIds) {
            User user = userRepository.findByUser(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            UserStatus status = userStatusRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", userId));

            summaryList.add(UserMapping.INSTANCE.userToSummary(user, status));
        }

        return summaryList;
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findByUser(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        binaryContentService.deleteBinaryContentByOwner(userId);
        userStatusService.deleteUserStatus(userId);
        cleanupUserChannels(userId);

        if (!userRepository.deleteUser(userId)) {
            throw new InvalidRequestException("user", "사용자 삭제에 실패했습니다");
        }


    }

    @Override
    public UserDto.Response createdUser(UserDto.Create createUserDto) {
        if (createUserDto.getEmail() != null) {
            userRepository.findByEmail(createUserDto.getEmail())
                    .ifPresent(user -> {
                        throw new DataConflictException("User", "email", createUserDto.getEmail());
                    });
        }

        User user = new User(createUserDto.getEmail(), createUserDto.getPassword());
        if (!userRepository.register(user)) {
            throw new InvalidRequestException("user", "사용자 등록에 실패했습니다");
        }
        
        userStatusService.createUserStatus(user.getId());
        return UserMapping.INSTANCE.userToResponse(user);
    }

    @Override
    public UserDto.Update updateUser(UUID userId, UserDto.Update updateUserDto) {
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (updateUserDto.getPassword() != null) {
            user.setPassword(updateUserDto.getPassword());
        }
        
        if (updateUserDto.getProfileImage() != null) {
            user.setProfileId(updateUserDto.getProfileImage());
        }
        
        if (!userRepository.updateUser(user)) {
            throw new InvalidRequestException("user", "사용자 정보 업데이트에 실패했습니다");
        }
        user.setUpdateAt();
        
        return UserMapping.INSTANCE.userToDto(user);
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.findByUser(UUID.fromString(userId)).isPresent();
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private void cleanupUserChannels(UUID userId) {
        User user = getUserOrThrow(userId);
        if (user.getBelongChannels() == null) {
            return;
        }

        for (UUID channelId : new ArrayList<>(user.getBelongChannels())) {
            channelRepository.findById(channelId).ifPresent(channel -> {

                channel.leaveChannel(userId);
                channelRepository.updateChannel(channel);

                readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                        .ifPresent(readStatus -> readStatusRepository.deleteReadStatus(readStatus.getId()));

            });

        }
    }

    

    public void leaveChannel(UUID userId, UUID channelId) {
        User user = getUserOrThrow(userId);
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));
            
        if (!user.getBelongChannels().removeIf(c -> c.equals(channelId))) {
            throw new InvalidRequestException("channel", "사용자가 해당 채널에 속해있지 않습니다");
        }
        
        userRepository.updateUser(user);
        channel.leaveChannel(userId);
        channelRepository.updateChannel(channel);

        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .ifPresent(readStatus -> readStatusRepository.deleteReadStatus(readStatus.getId()));
        messageRepository.deleteMessage(userId);
        readStatusRepository.deleteAllByUserId(userId);
        binaryContentRepository.delete(userId);
    }
}

