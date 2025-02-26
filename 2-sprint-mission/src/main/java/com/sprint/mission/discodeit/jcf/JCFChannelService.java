package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;
    private final Map<UUID, Channel> data;
    private final UserService userService;

    private JCFChannelService(UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
    }

    public static JCFChannelService getInstance(UserService userService) {
        if(instance == null) {
            synchronized (JCFChannelService.class) {
                if(instance == null) {
                    instance = new JCFChannelService(userService);
                }
            }
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        //데이터 유효성 확인
        this.channelValidationCheck(channel);
        //요청자 확인, 요청자의 권한 확인
        User user = userService.selectUserById(channel.getUserId())
                                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다."));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("채널 개설은 관리자만 가능합니다.");
        }
        //create
        data.put(channel.getId(), channel);
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인, 요청자의 권한 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + userId));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("멤버 추가는 관리자만 가능합니다.");
        }
        //채널 확인
        Channel channel = this.selectChannelById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다. : " + id));
        //멤버 확인
        for (UUID memberId : userMembers) {
            if (channel.getUserMembers().contains(memberId)) {
                throw new RuntimeException("이미 채널에 포함된 사용자입니다: " + memberId);
            }
            userService.selectUserById(memberId)
                    .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + memberId));
        }
        //addMember
        userMembers.forEach(channel::addMember);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인, 요청자의 권한 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + userId));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("멤버 삭제는 관리자만 가능합니다.");
        }
        //채널 확인
        Channel channel = this.selectChannelById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다. : " + id));
        //멤버 확인
        for (UUID memberId : userMembers) {
            if (!channel.getUserMembers().contains(memberId)) {
                throw new RuntimeException("채널에 존재하지 않는 사용자입니다: " + memberId);
            }
        }
        //removeMember
        userMembers.forEach(channel::removeMember);
    }

    @Override
    public Optional<Channel> selectChannelById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> selectAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        //데이터 유효성 확인
        if (id == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        //요청자 확인, 요청자의 권한 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + userId));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("채널 수정은 관리자만 가능합니다.");
        }
        //채널 확인
        Channel channel = this.selectChannelById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다. : " + id));
        //update
        if (name != null) {
            if (name.length() > 20) {
                throw new IllegalArgumentException("채널명은 20자 미만이어야 합니다.");
            }
            channel.updateName(name);
        }
        if (category != null) {
            if (channel.getCategory() != null && category.length() > 20) {
                throw new IllegalArgumentException("카테고리명은 20자 미만이어야 합니다.");
            }
            channel.updateCategory(category);
        }
        if (type != null) {
            channel.updateType(type);
        }
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        //데이터 유효성 확인
        if (id == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        //요청자 확인, 요청자의 권한 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다."));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("채널 삭제는 관리자만 가능합니다.");
        }
        //채널 확인
        Channel channel = this.selectChannelById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다."));
        //delete
        data.remove(channel.getId());
    }

    /*******************************
     * Channel Data Validation check
     *******************************/
    private void channelValidationCheck(Channel channel){
        // 1. null check
        if (channel == null) {
            throw new IllegalArgumentException("channel 객체가 null입니다.");
        }
        if (channel.getId() == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        if (channel.getType() == null) {
            throw new IllegalArgumentException("Type 값이 없습니다.");
        }
        if (channel.getName() == null || channel.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("채널명이 없습니다.");
        }
        if (channel.getUserId() == null) {
            throw new IllegalArgumentException("채널 개설자의 ID가 없습니다.");
        }
        if (channel.getWritePermission() == null) {
            throw new IllegalArgumentException("writePermission 값이 없습니다.");
        }
        //2. 카테고리명 길이 check
        if (channel.getCategory() != null && channel.getCategory().length() > 20) {
            throw new IllegalArgumentException("카테고리명은 20자 미만이어야 합니다.");
        }
        //3. 채널명 길이 check
        if (channel.getName().length() > 20) {
            throw new IllegalArgumentException("채널명은 20자 미만이어야 합니다.");
        }
    }
}
