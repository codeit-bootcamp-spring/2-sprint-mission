package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.ValidationUtil;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    
    // 싱글톤 인스턴스
    private static UserChannelService instance;
    
    // private 생성자로 변경하여 외부에서 인스턴스 생성을 제한
    private UserChannelService(
            UserRepository userRepository, 
            ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized UserChannelService getInstance(
            UserRepository userRepository,
            ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new UserChannelService(userRepository, channelRepository);
        }
        return instance;
    }
    
    public void addUserToChannel(UUID userId, Channel channel) {
        // 채널이 저장소에 있는지 확인
        if (channelRepository.findChannelById(channel.getChannelId()).isEmpty()) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다: " + channel.getChannelId());
        }
        
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        
        // 속해있는지 확인
        boolean userInChannel = user.getBelongChannels().contains(channel.getChannelId());
        boolean channelInUser = channel.getUserList().contains(userId);
        
        //한쪽이라도 속할 경우 수정
        if (userInChannel || channelInUser) {
            channel.getUserList().add(userId);
            user.getBelongChannels().add(channel.getChannelId());
            
            // 변경사항 저장
            channelRepository.updateChannel(channel);
            userRepository.updateUser(user);
        }

        // 이미 속할 경우
        if (userInChannel && channelInUser) {
         throw new IllegalStateException("사용자 " + userId + "는 이미 채널 " + channel.getChannelId() + "에 속해 있습니다.");   
        }
        channel.joinChannel(userId);
        user.addBelongChannel(channel.getChannelId());
        // 변경사항 저장
        channelRepository.updateChannel(channel);
        userRepository.updateUser(user);
    }


    public void removeUserFromChannel(UUID userId, UUID channelId) {
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));

        // 채널 소유자인 경우 제거 불가
        if (channel.getOwnerID().equals(userId)) {
            throw new IllegalStateException("채널 소유자는 채널에서 나갈 수 없습니다. 채널을 삭제하세요.");
        }

        // 사용자가 채널에 속해 있는지 확인
        boolean userInChannel = channel.getUserList().contains(userId);
        boolean channelInUser = user.getBelongChannels().contains(channelId);

        if (!userInChannel && !channelInUser) {
            throw new IllegalStateException("사용자 " + userId + "는 채널 " + channelId + "에 속해 있지 않습니다.");
        }

        // 백업
        Set<UUID> userListBackup = new HashSet<>(channel.getUserList());
        Set<UUID> channelListBackup = new HashSet<>(user.getBelongChannels());

        try {
            channel.leaveChannel(userId);
            user.removeBelongChannel(channelId);

            // 변경사항 저장
            channelRepository.updateChannel(channel);
            userRepository.updateUser(user);
        } catch (Exception e) {
            // 백업으로 복원
            channel.getUserList().clear();
            channel.getUserList().addAll(userListBackup);

            user.getBelongChannels().clear();
            user.getBelongChannels().addAll(channelListBackup);

            throw new RuntimeException("채널 사용자 제거 중 오류 발생: " + e.getMessage(), e);
        }
    }
    public void cleanupUserChannels(UUID userId) {
        //호출 전 검증 완료
        User user = userRepository
        .findByUser(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Set<UUID> userChannels = new HashSet<>(user.getBelongChannels());
        for (UUID channelId : userChannels) {
            cleanupUserFromChannel(user, channelId);
        }
        //비움
        user.getBelongChannels().clear();
    }

    private void cleanupUserFromChannel(User user, UUID channelId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        ValidationUtil.validateNotNull(user, "사용자");
        
        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
        if (channel == null) {
            return;
        }
        
        UUID userId = user.getId();
        
        if (channel.getOwnerID().equals(userId)) {
            // 사용자가 채널 소유자인 경우 채널 삭제
            cleanupChannelUsers(channelId);
            channelRepository.deleteChannel(channelId);
        } else {
            try {
                // 일반 사용자인 경우 채널에서만 제거
                channel.leaveChannel(userId);
                // 변경사항 저장
                channelRepository.updateChannel(channel);
            } catch (Exception e) {
                throw new RuntimeException("채널 삭제 중 오류 발생: " + e.getMessage());
            }
        }
    }
    
    public void cleanupChannelUsers(UUID channelId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        
        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
        if (channel == null) {
            return;
        }
        
        Set<UUID> channelUsers = new HashSet<>(channel.getUserList());
        
        for (UUID userId : channelUsers) {
            User user = userRepository.findByUser(userId).orElse(null);
            if (user != null) {
                user.removeBelongChannel(channelId);
                // 변경사항 저장
                userRepository.updateUser(user);
            }
        }
    }
} 