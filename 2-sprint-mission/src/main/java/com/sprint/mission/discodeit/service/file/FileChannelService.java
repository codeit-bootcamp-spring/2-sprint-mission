package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static volatile FileChannelService instance;
    private final UserService userService;
    private final ChannelRepository channelRepository;

    private FileChannelService(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    public static FileChannelService getInstance(UserService userService, ChannelRepository channelRepository) {
        if(instance == null) {
            synchronized (FileChannelService.class) {
                if(instance == null) {
                    instance = new FileChannelService(userService, channelRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        //요청자 확인
        User user = findUserOrThrow(channel.getUserId());
        //개설 권한 확인
        validatePermission(user);
        //create
        channelRepository.createChannel(channel);
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //멤버 추가 권한 확인
        validatePermission(user);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //멤버 확인
        validateMembers(userMembers);
        //addMember
        channelRepository.addMembers(id, userMembers, userId);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //멤버 삭제 권한 확인
        validatePermission(user);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //멤버 확인
        validateMembers(userMembers);
        //removeMember
        channelRepository.removeMembers(id, userMembers, userId);
    }

    @Override
    public Optional<Channel> selectChannelById(UUID id) {
        return channelRepository.selectChannelById(id);
    }

    @Override
    public List<Channel> selectAllChannels() {
        return channelRepository.selectAllChannels();
    }

    @Override
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        //id 유효성 확인
        validateId(id);
        //요청자 확인
        User user = findUserOrThrow(userId);
        //수정 권한 확인
        validatePermission(user);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //update
        channelRepository.updateChannel(id, name, category, type, userId);
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        //id 유효성 확인
        validateId(id);
        //요청자 확인
        User user = findUserOrThrow(userId);
        //삭제 권한 확인
        validatePermission(user);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //delete
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
            userService.selectUserById(memberId)
                    .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + memberId));
        }
    }

    private void validateId(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("채널 ID 값이 없습니다.");
        }
    }

    private Channel findChannelOrThrow(UUID channelId){
        return selectChannelById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널이 존재하지 않습니다. : " + channelId));
    }

    private User findUserOrThrow(UUID userId) {
        return userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다. : " + userId));
    }

}
