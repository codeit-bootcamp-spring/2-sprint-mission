package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class BasicChannelService implements ChannelService {

    private static volatile BasicChannelService instance;
    private final UserService userService;
    private final ChannelRepository channelRepository;

    private BasicChannelService(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(UserService userService, ChannelRepository channelRepository) {
        if(instance == null) {
            synchronized (BasicChannelService.class) {
                if(instance == null) {
                    instance = new BasicChannelService(userService, channelRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        //데이터 유효성 확인
        channelValidationCheck(channel);
        //요청자 확인
        User user = findUserOrThrow(channel.getUserId());
        //개설 권한 확인
        validateUserPermission(user, ActionType.CREATE_CHANNEL);
        //create
        channelRepository.createChannel(channel);
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //멤버 추가 권한 확인
        validateUserPermission(user, ActionType.ADD_MEMBER);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //멤버 확인
        validateMembers(userMembers, channel, ActionType.ADD_MEMBER);
        //addMember
        channelRepository.addMembers(id, userMembers, userId);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //멤버 삭제 권한 확인
        validateUserPermission(user, ActionType.REMOVE_MEMBER);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //멤버 확인
        validateMembers(userMembers, channel, ActionType.REMOVE_MEMBER);
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
        //데이터 유효성 확인
        validateId(id);
        //요청자 확인
        User user = findUserOrThrow(userId);
        //수정 권한 확인
        validateUserPermission(user, ActionType.EDIT_CHANNEL);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //update
        if (name != null && !name.equals(channel.getName())) {
            validateName(name);
        }
        if (category != null && !category.equals(channel.getCategory())) {
            validateCategory(category);
        }
        if (name != null || category != null || type != null) {
            channelRepository.updateChannel(id, name, category, type, userId);
        }
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        //데이터 유효성 확인
        validateId(id);
        //요청자 확인
        User user = findUserOrThrow(userId);
        //삭제 권한 확인
        validateUserPermission(user, ActionType.DELETE_CHANNEL);
        //채널 확인
        Channel channel = findChannelOrThrow(id);
        //delete
        channelRepository.deleteChannel(id, userId);
    }

    /*******************************
     * Validation check
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
        validateCategory(channel.getCategory());
        //3. 채널명 길이 check
        validateName(channel.getName());
    }

    private void validateUserPermission(User user, ActionType action) {
        if (user.getRole() != UserRole.ADMIN) {
            String actionMessage = switch (action) {
                case CREATE_CHANNEL -> "채널 개설";
                case EDIT_CHANNEL -> "채널 수정";
                case DELETE_CHANNEL -> "채널 삭제";
                case ADD_MEMBER -> "멤버 추가";
                case REMOVE_MEMBER -> "멤버 삭제";
                default -> "해당 작업";
            };
            throw new RuntimeException(actionMessage + "은(는) 관리자만 가능합니다.");
        }
    }

    private void validateMembers(Set<UUID> userMembers, Channel channel, ActionType action) {
        for (UUID memberId : userMembers) {
            userService.selectUserById(memberId)
                    .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + memberId));

            if (action == ActionType.ADD_MEMBER) {
                if (channel.getUserMembers().contains(memberId)) {
                    throw new RuntimeException("이미 채널에 포함된 사용자입니다: " + memberId);
                }
            } else if ((action == ActionType.REMOVE_MEMBER)) {
                if (!channel.getUserMembers().contains(memberId)) {
                    throw new RuntimeException("채널에 존재하지 않는 사용자입니다: " + memberId);
                }
            }
        }
    }

    private void validateCategory(String category){
        if (category != null && category.length() > 20) {
            throw new IllegalArgumentException("카테고리명은 20자 미만이어야 합니다.");
        }
    }

    private void validateName(String name){
        if (name != null && name.length() > 20) {
            throw new IllegalArgumentException("채널명은 20자 미만이어야 합니다.");
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
