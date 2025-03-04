package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    private JCFChannelService() {
        data = new ArrayList<>();
    }

    // 싱글톤 패턴 적용 - 지금은 javaApplication에서만 사용되는데 굳이 적용할 필요가 있을까? -> 나중을 위해 적용해놓자.
    // 아직까진 멀티 쓰레드 환경이 아니므로 게으른 초기화 사용
    private static ChannelService channelService;

    public static ChannelService getInstance() {
        if (channelService == null) {
            channelService = new JCFChannelService();
        }
        return channelService;
    }


    // =================================== 채널 생성 ===================================
    // 채널 생성
    @Override
    public void createChannel(Channel channel) {
        validateCreateChannel(channel);
        checkDuplicateChannelName(channel.getChannelName());
        data.add(channel);
    }

    private void validateCreateChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("생성하려는 채널 정보가 존재하지 않습니다.");
        }
        // 채널명과 채널주인은 필수입력 항목
        if (channel.getChannelName() == null || channel.getChannelName().isBlank() || channel.getOwner() == null) {
            throw new IllegalArgumentException("필수 입력 항목이 입력되지 않았습니다. (channelName, owner)");
        }
    }

    private void checkDuplicateChannelName(String channelName) {
        // 중복 채널 방지, 채널 이름으로 채널을 찾거나 수정하기 때문에 겹치면 안됨
        if (data.stream().anyMatch(ch -> ch.getChannelName().equals(channelName))) {
            throw new IllegalStateException("중복된 channelName 입니다.");
        }
    }


    // =================================== 채널 조회 ===================================
    // 채널 단건 조회
    @Override
    public Channel getChannel(String channelName) {
        if (channelName == null || channelName.isBlank()) {
            throw new IllegalArgumentException("channelName이 입력되지 않았습니다.");
        }

        Channel findChannel = data.stream()
                .filter(ch -> ch.getChannelName().equals(channelName))
                .findFirst() // 리턴타입이 Optional이니까 Optional<Channel>로 받아주거나, orElseThrow로 Optional 벗겨내기
                .orElseThrow(() -> new NoSuchElementException("해당 channelName을 가진 채널은 존재하지 않습니다."));

        return findChannel;
    }

    // 채널 다건 조회
    @Override
    public List<Channel> getAllChannels() {
        return data;
    }


    // =================================== 채널 수정 ===================================
    // 유저 추가
    @Override
    public void addUsersToChannel(User requestUser, User user, String channelName) {
        // 매개변수 - Channel 객체로 가져오는 것 보다 channelName만 가져오는게 나을지도?..
        // 현재는 메소드에서 channel.channelName만 사용하긴하는데, Channel을 쓰는게 확장성이 더 좋을 것 같기도
        // <-> 객체 째로 가져오면 넘 무겁지 않나? (고민)

        Channel findChannel = getChannel(channelName);
        validateUpdateChannelAndUser(requestUser, user, findChannel);
        updateChannelInfo(findChannel);
        findChannel.addUsers(user);

        // user의 필드에도 채널 추가
        user.addChannel(findChannel);
    }

    // 유저 삭제
    @Override
    public void removeUsersFromChannel(User requestUser, User user, String channelName) {
        Channel findChannel = getChannel(channelName);
        validateUpdateChannelAndUser(requestUser, user, findChannel);
        updateChannelInfo(findChannel);
        findChannel.removeUsers(user);

        // user의 필드에도 채널 삭제
        user.removeChannel(findChannel);
        // user의 필드에도 해당 채널의 메시지 전부 삭제
        deleteUserMessagesFromChannel(findChannel, user);
    }

    private void validateUpdateChannelAndUser(User requestUser, User user, Channel channel) {
        if (user == null) {
            throw new IllegalArgumentException("수정하려는 유저 정보가 입력되지 않았습니다.");
        }

        // 채널 수정 시 요청을 하는 User(requestUser)가 해당 채널의 주인이어야한다.
        if (!requestUser.equals(channel.getOwner())) {
            throw new IllegalStateException("수정할 채널의 주인이 아닙니다.");
        }
    }

    private void updateChannelInfo(Channel channel) {
        channel.updateUpdatedAt(System.currentTimeMillis());
    }

    private void deleteUserMessagesFromChannel(Channel channel, User user) {
        // user.getMessages().stream().filter(m -> m.getChannel().equals(findChannel)).forEach(m -> user.deleteMessage(m));
        // -> Stream 순회 도중 원본 컬렉션이 바뀌면 ConcurrentModificationException 발생하므로 아래 코드로 수정
        List<Message> findMessages = user.getMessages().stream().filter(m -> m.getChannel().equals(channel)).collect(Collectors.toList());
        findMessages.stream().forEach(m -> m.getSender().deleteMessage(m));
        // 해당 메시지를 List로 받고, 그 List를 돌면서 다시 삭제하면 해결
    }


    // =================================== 채널 삭제 ===================================
    // 채널 삭제
    @Override
    public void deleteChannel(User requestUser, String channelName) {
        Channel findChannel = getChannel(channelName);
        // 요청한 요저가 채널 주인인지 확인
        checkChannelOwner(requestUser, findChannel);
        // 채널 삭제 시 User의 Channel, Message 목록에서도 삭제
        deleteUserChannelAndMessage(findChannel);
        data.remove(findChannel);
    }

    public void checkChannelOwner(User user, Channel channel) {
        // 채널 삭제 시 요청을 하는 User(requestUser)가 해당 채널의 주인이어야한다.
        if (!user.getUsername().equals(channel.getOwner().getUsername())) {
            throw new IllegalStateException("삭제할 채널의 주인이 아닙니다.");
        }
    }

    public void deleteUserChannelAndMessage(Channel channel) {
        // 채널 삭제 시 User의 Channel 목록에서도 삭제
        List<User> users = channel.getUsers().stream().collect(Collectors.toList());
        users.stream().forEach(u -> u.removeChannel(channel));

        // 채널 삭제 시 User의 Message 목록에서도 삭제
        List<Message> messages = channel.getMessages(); // 해당 채널의 메시지들을 찾고,
        users.stream().forEach(u -> messages.stream().forEach(m -> u.deleteMessage(m)));  // 해당 유저에서 메시지를 삭제
        // cascade를 사용하면 해결할 수 있지만.. JPA를 사용하지 않는 자바 수준에서는 직접 지워주는게 답인지 고민
        // 이 부분에 대한 예외 처리는? 예외 처리에 대한 공부가 필요.
    }


}
