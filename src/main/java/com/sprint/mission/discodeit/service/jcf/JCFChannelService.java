package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static final JCFChannelService INSTANCE = new JCFChannelService(JCFUserService.getInstance());
    public JCFUserService ui;
    final Map<UUID, Channel> data;

    private JCFChannelService(JCFUserService ui) {
        data = new HashMap<>();
        this.ui = ui;
    }

    public static JCFChannelService getInstance() {
        return INSTANCE;
    }

    @Override
    public void signUp(String name, UUID userUuid) {
        Channel entity = findChannelEntity(name);
        if (entity == null) {
            System.out.println("[Error] 가입하려는 채널이 존재하지 않습니다.");
            return;
        }
        if (isSignUp(name, userUuid)) {
            System.out.println("[Error] 이미 가입된 채널입니다.");
            return;
        }
        entity.updateMemberName(getMemberName(userUuid));
    }

    @Override
    public UUID create(String category, String name, String introduction, UUID memberUuid, UUID ownerUuid) {
        if (isChannelCheck(name)) {
            System.out.println("[Error] 동일한 채널명이 존재합니다.");
            return null;
        }
        Channel channel = new Channel(category, name, introduction, getMemberName(memberUuid), getOwnerName(ownerUuid));
        data.put(channel.getUuid(), channel);
        System.out.println("[Info] 채널 생성이 완료 되었습니다.");
        return channel.getUuid();
    }
    @Override
    public Channel read(String name) {
        if (!isChannelCheck(name)) {
            System.out.println("[Error] 채널이 존재 하지 않습니다.");
        }
        return findChannelEntity(name);
    }

    @Override
    public List<Channel> readAll(List<String> nameList) {
        if (!isChannelCheck(nameList)) {
            System.out.println("[Error] 채널이 존재 하지 않습니다");
        }
        return findChannelEntity(nameList);
    }

    public List<Channel> readAll() {
        return findChannelEntity();
    }

    @Override
    public void update(String origin, String category, String name, String introduction) {

        Channel originChannel = data.get(findChannelUuid(origin));

        if (!isChannelCheck(origin)) {
            System.out.println("[Error] 채널이 존재 하지 않습니다");
            return;
        }
        if (!category.isEmpty()) {
            originChannel.updateCategory(category);
        }
        if (!name.isEmpty()) {
            originChannel.updateName(name);
        }
        if (!name.isEmpty()) {
            originChannel.updateIntroduction(introduction);
        }
    }

    @Override
    public void delete(String name, UUID userUuid) {
        if (!isChannelCheck(name)) {
            System.out.println("[Error] 채널이 존재 하지 않습니다");
            return;
        } else if (!isChannelOwner(userUuid)) {
            System.out.println("[Error] 삭제가 불가능 합니다 (권한부족)");
            return;
        }
        data.remove(findChannelUuid(name));
        System.out.println("[Info] 정상적으로 삭제 되었습니다.");
    }

    @Override
    public List<String> getOwnerChannelName(UUID userUuid) {
        List<String> cn = findOwnerChannelName(userUuid);
        if (cn.isEmpty()) {
            return null;
        }
        return cn;
    }

    public String getMemberName(UUID memberUuid) {
        return JCFUserService.getInstance().getUserName(memberUuid);
    }

    public String getChannelName(UUID channelUuid) {
        return findChannelName(channelUuid);
    }

    public UUID getChannelUuid(String channelName) {
        return findChannelUuid(channelName);
    }

    public List<String> getUserChannels(UUID userUuid) {
        return findMemberChannelName(userUuid);
    }

    public String getOwnerName(UUID ownerUuid) {
        return JCFUserService.getInstance().getUserName(ownerUuid);
    }

    private boolean isChannelCheck(String name) {
        return data.values().stream()
                .anyMatch(c -> c.getName().equals(name));
    }

    private boolean isChannelCheck(List<String> name) {
        return data.values().stream()
                .anyMatch(cl -> name.contains(cl.getName()));
    }

    private Channel findChannelEntity(String name) {
        return data.values().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    private List<Channel> findChannelEntity(List<String> name) {
        return data.values().stream()
                .filter(cl -> name.contains(cl.getName()))
                .toList();
    }
    private List<Channel> findChannelEntity() {
        List<Channel> info = data.values().stream().toList();
        if (info.isEmpty()) {
            System.out.println("생성된 채널이 없습니다");
            return null;
        }
        return info;
    }

    private UUID findChannelUuid(String name) {
        return data.values().stream()
                .filter(c -> c.getName().equals(name))
                .map(Channel::getUuid)
                .findFirst()
                .orElse(null);
    }

    private List<String> findOwnerChannelName(UUID userUuid) {
        return data.values().stream()
                .filter(c -> c.getOwnerName().equals(getOwnerName(userUuid)))
                .map(Channel::getName)
                .toList();
    }

    private List<String> findMemberChannelName(UUID userUuid) {
        String userName = getMemberName(userUuid);
        return data.values().stream()
                .filter(c -> c.getMemberName().contains(userName))
                .map(Channel::getName)
                .toList();
    }

    private String findChannelName(UUID channelUuid) {
        return data.get(channelUuid).getName();
    }

    private boolean isChannelOwner(UUID userUuid) {
        return data.values().stream()
                .anyMatch(c -> c.getOwnerName().equals(getOwnerName(userUuid)));
    }

    private boolean isSignUp(String name, UUID userUuid) {
        return data.values().stream()
                .anyMatch(c -> c.getName().equals(name) && c.getMemberName().contains(getMemberName(userUuid)));
    }
}
