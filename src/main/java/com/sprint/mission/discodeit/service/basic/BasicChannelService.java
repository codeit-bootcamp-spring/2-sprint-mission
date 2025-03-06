package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static BasicChannelService instance;
    private final ChannelRepository channelRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static synchronized BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new BasicChannelService(channelRepository);
        }
        return instance;
    }

    @Override
    public void create(Channel channel) {
        System.out.println("==== 채널 생성 중... ===");
        if (channel == null) {
            throw new IllegalArgumentException("채널 정보가 NULL입니다.");
        }

        if (channelRepository.find(channel.getId()) != null) {
            throw new RuntimeException("채널이 이미 존재합니다.");
        }
        channelRepository.create(channel);
        System.out.println("[" + channel +"] 채널 생성 완료 " + channel.getId());
    }

    @Override
    public Channel find(UUID id) {
        System.out.println("==== 채널(단건) 조회 중... ===");
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (channelRepository.find(id) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        System.out.println("선택한 채널을 조회합니다.");
        return channelRepository.find(id);
    }

    @Override
    public List<Channel> findAll() {
        System.out.println("==== 채널(다건) 조회 중... ===");
        if (channelRepository.findAll().isEmpty()) {
            System.out.println("등록된 채널이 없습니다.");
        }
        System.out.println("모든 채널을 조회합니다.");
        return channelRepository.findAll();
    }

    @Override
    public void update(Channel channel) {
        System.out.println("==== 채널 수정 중... ===");
        if (channel == null) {
            throw new IllegalArgumentException("채널 정보가 NULL입니다.");
        }

        if (channelRepository.find(channel.getId()) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channelRepository.update(channel);
        System.out.println("[" + channel +"] 채널 수정 완료 " + channel.getId());
    }

    @Override
    public void delete(UUID id) {
        System.out.println("==== 채널 삭제 중... ===");
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (channelRepository.find(id) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channelRepository.delete(id);
        System.out.println("채널 삭제 완료");
    }

    @Override
    public void addMember(UUID channelId, User user) {
        System.out.println("==== 채널에 유저 등록 중... ===");
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.addMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에 등록되었습니다.");
    }

    @Override
    public void removeMember(UUID channelId, User user) {
        System.out.println("==== 채널에서 유저 삭제 중... ===");
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.removeMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에서 제거되었습니다.");
    }

    @Override
    public List<User> findMembers(UUID channelId) {
        System.out.println("==== 채널에서 유저 조회 중... ===");
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        ArrayList<User> members = new ArrayList<>(channel.getMembers());
        System.out.println("채널 [" + channel.getChannelName() + "]에 등록된 유저 목록: " + members);
        return members;
    }
}
