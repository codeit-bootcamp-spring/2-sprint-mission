package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapping.UserMapping;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
@Validated
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public BasicUserService(
            @Qualifier("basicUserRepository") UserRepository userRepository,
            @Qualifier("basicUserStatusRepository") UserStatusRepository userStatusRepository,
            @Qualifier("basicBinaryContentRepository") BinaryContentRepository binaryContentRepository,
            @Qualifier("basicChannelRepository") ChannelRepository channelRepository,
            @Qualifier("basicReadStatusRepository") ReadStatusRepository readStatusRepository,
            @Qualifier("basicMessageRepository") MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.channelRepository = channelRepository;
        this.readStatusRepository = readStatusRepository;
        this.messageRepository = messageRepository;
    }


    @Override
    public UserDto.Summary findByUserId(UUID id) {
        User user = userRepository.findByUser(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
        UserStatus userStatus = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
        return UserMapping.INSTANCE.userToSummary(user, userStatus);
    }

    @Override
    public List<UserDto.Summary> findByAllUsersId() {
        Set<UUID> userIds = userRepository.findAllUsers();
        List<UserDto.Summary> summaryList = new ArrayList<>();

        for (UUID userId : userIds) {
            User user = userRepository.findByUser(userId)
                    .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
            UserStatus status = userStatusRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("상태 정보를 찾을 수 없습니다"));

            summaryList.add(UserMapping.INSTANCE.userToSummary(user, status));
        }

        return summaryList;
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findByUser(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        // 1. 사용자가 소유한 바이너리 콘텐츠 삭제
        deleteUserBinaryContents(userId);
        
        // 2. 사용자 상태 삭제
        deleteUserStatus(userId);
        
        // 3. 사용자 채널 관련 정보 정리
        cleanupUserChannels(userId);
            
        // 4. 사용자 삭제
        if (!userRepository.deleteUser(userId)) {
            throw new RuntimeException("사용자 삭제 실패");
        }
    }

    @Override
    public UserDto.Response createdUser(UserDto.Create createUserDto) {
        // 이메일 유효성 검증
        if (createUserDto.getEmail() != null) {
            checkEmailDuplication(createUserDto.getEmail());
        }

        User user = new User(createUserDto.getEmail(), createUserDto.getPassword());
        
        if (!userRepository.register(user)) {
            throw new RuntimeException("사용자 저장 실패");
        }

        return UserMapping.INSTANCE.userToResponse(user);
    }

    @Override
    public UserDto.Update updateUser(UserDto.Update updateUserDto) {
        // 사용자 찾기
        UUID userId = updateUserDto.getId();
        Optional<User> userOptional = userRepository.findByUser(userId);
        
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
        
        User user = userOptional.get();
        
        // 비밀번호 업데이트
        if (updateUserDto.getPassword() != null) {
            user.setPassword(updateUserDto.getPassword());
        }
        
        // 프로필 이미지 업데이트
        if (updateUserDto.getProfileImage() != null) {
            user.setProfileId(updateUserDto.getProfileImage());
        }
        
        // 사용자 정보 업데이트
        if (!userRepository.updateUser(user)) {
            throw new RuntimeException("사용자 정보 업데이트 실패");
        }
        
        return UserMapping.INSTANCE.userToDto(user);
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findByUser(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
    }

    private UserStatus getUserStatusOrThrow(UUID userId) {
        return userStatusRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
    }

    // 이메일 중복 검사 메서드
    private void checkEmailDuplication(String email) {
        // findByEmail 메서드 사용 (인터페이스와 일치)
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + email);
        }
    }
    
    // 사용자의 채널 관련 정보 정리
    private void cleanupUserChannels(UUID userId) {
        // 사용자가 참여 중인 모든 채널 찾기
        User user = getUserOrThrow(userId);
        
        for (UUID channelId : new ArrayList<>(user.getBelongChannels())) {
            // 채널에서 사용자 제거
            channelRepository.findById(channelId).ifPresent(channel -> {
                channel.leaveChannel(userId);
                channelRepository.updateChannel(channel);
                
                // 해당 채널의 ReadStatus 삭제
                readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                    .ifPresent(readStatus -> readStatusRepository.deleteReadStatus(readStatus.getId()));
            });
        }
    }
    
    // 사용자 상태 삭제
    private void deleteUserStatus(UUID userId) {
        userStatusRepository.findById(userId)
            .ifPresent(status -> userStatusRepository.delete(status.getId()));
    }
    
    // 사용자 바이너리 콘텐츠 삭제
    private void deleteUserBinaryContents(UUID userId) {
        // 프로필 이미지 등 사용자 관련 바이너리 콘텐츠 삭제
        User user = getUserOrThrow(userId);
        if (user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }
        
        // 사용자가 작성한 메시지의 첨부파일 삭제
        messageRepository.findAll().stream()
            .filter(message -> message.getAuthorId().equals(userId))
            .forEach(message -> {
                for (UUID attachmentId : new ArrayList<>(message.getAttachmentIds())) {
                    binaryContentRepository.delete(attachmentId);
                }
            });
    }
    

    
    // 채널 떠나기
    public void leaveChannel(UUID userId, UUID channelId) {
        User user = getUserOrThrow(userId);
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다"));
            
        // 사용자의 채널 목록에서 제거
        if (user.getBelongChannels().removeIf(c -> c.equals(channelId))) {
            userRepository.updateUser(user);
        } else {
            throw new IllegalStateException("유저가 채널에 존재하지 않습니다");
        }
        
        // 채널의 사용자 목록에서 제거
        channel.leaveChannel(userId);
        channelRepository.updateChannel(channel);

        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .ifPresent(readStatus -> readStatusRepository.deleteReadStatus(readStatus.getId()));
        messageRepository.deleteMessage(userId);
        readStatusRepository.deleteAllByUserId(userId);
        binaryContentRepository.delete(userId);
    }
}

