package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {
        data = new ArrayList<>();
    }

    // 채널 생성
    @Override
    public void createChannel(Channel channel) {
        if(channel == null) {
            throw new IllegalArgumentException("생성하려는 채널 정보가 올바르지 않습니다.");
        }
        // 채널명과 채널주인은 필수입력 항목
        if(channel.getChannelName().isEmpty() || channel.getOwner() == null) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }
        // 중복 채널 방지, 채널 이름으로 채널을 찾거나 수정하기 때문에 겹치면 안됨
        if(data.stream().anyMatch(ch -> ch.getChannelName().equals(channel.getChannelName()))) {
            throw new IllegalArgumentException("중복된 채널명 입니다.");
        }

        data.add(channel);
    }

    // 채널 단건 조회
    @Override
    public Channel getChannel(String channelName) {
        Channel findChannel = data.stream()
                .filter(ch -> ch.getChannelName().equals(channelName))
                .findFirst() // 리턴타입이 Optional이니까 Optional<Channel>로 받아주거나, orElseThrow로 Optional 벗겨내기
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        return findChannel;
    }

    // 채널 다건 조회
    @Override
    public List<Channel> getAllChannels() {
        return data;
    }

    // 채널 수정
    // 유저 추가
    @Override
    public void addUsersToChannel(User requestUser, User user, String channelName) {
        // 매개변수 - Channel 객체로 가져오는 것 보다 channelName만 가져오는게 나을지도?..
        // 현재는 메소드에서 channel.channelName만 사용하긴하는데, Channel을 쓰는게 확장성이 더 좋을 것 같기도
        // <-> 객체 째로 가져오면 넘 무겁지 않나? (고민)
        if (user == null) {
            throw new IllegalArgumentException("추가할 유저가 존재하지 않습니다.");
        }

        Channel findChannel = getChannel(channelName);

        // 채널 수정 시 요청을 하는 User(requestUser)가 해당 채널의 주인이어야한다.
        if (!requestUser.equals(findChannel.getOwner())) {
            throw new IllegalArgumentException("수정할 채널의 주인이 아닙니다.");
        }

        findChannel.updateUpdatedAt(System.currentTimeMillis());
        findChannel.addUsers(user);
    }

    // 유저 삭제
    @Override
    public void removeUsersFromChannel(User requestUser, User user, String channelName) {
        if (user == null) {
            throw new IllegalArgumentException("삭제할 유저가 존재하지 않습니다.");
        }

        Channel findChannel = getChannel(channelName);

        // 채널 수정 시 요청을 하는 User(requestUser)가 해당 채널의 주인이어야한다.
        if (!requestUser.getUsername().equals(findChannel.getOwner().getUsername())) {
            throw new IllegalArgumentException("수정할 채널의 주인이 아닙니다.");
        }

        findChannel.updateUpdatedAt(System.currentTimeMillis());
        findChannel.removeUsers(user);
    }


    // 채널 삭제
    @Override
    public void deleteChannel(User requestUser, String channelName) {
        Channel findChannel = getChannel(channelName);

        // 채널 삭제 시 요청을 하는 User(requestUser)가 해당 채널의 주인이어야한다.
        if (!requestUser.getUsername().equals(findChannel.getOwner().getUsername())) {
            throw new IllegalArgumentException("삭제할 채널의 주인이 아닙니다.");
        }

        data.remove(findChannel);
    }
}
