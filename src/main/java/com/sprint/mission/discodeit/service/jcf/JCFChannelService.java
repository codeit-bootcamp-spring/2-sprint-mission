package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData;
    private static JCFChannelService INSTANCE = new JCFChannelService();

    //생성할때 초기화
    public JCFChannelService() {
        this.channelData = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        // instance 가 null 일 때만 생성
        if (INSTANCE == null) {
            INSTANCE = new JCFChannelService();
        }
        return INSTANCE;
    }

    @Override
    public Channel createChannel(String channelName) {
        if (!validateParameter(channelName)) {
            return null;
        }
        Channel channel = new Channel(channelName); //채널 객체 생성
        channelData.put(channel.getId(), channel); // (키: 해당 객체의 id, 값: 해당 객체 전체)
        System.out.println("채널방이 생성됐습니다 : " +  channel);
        return channel;
    }

    @Override
    public void getChannelInfo(String channelName) {
        Channel channel = findChannelEntry(channelName);
        if (channel == null) {
            System.out.println("해당 채팅방이 존재하지 않습니다.");
            return;
        }
        System.out.println("채팅방 정보 조회: " + channel);
    }

    @Override
    public void getAllChannelData() {
        if (channelData.isEmpty()) {
            System.out.println("채팅방 정보가 없습니다.");
            return;
        }
        System.out.println("모든 채팅방 정보: " + channelData);
    }

    @Override
    public void updateChannelName(String oldChannelName, String newChannelName) {
        Channel oldChannel = findChannelEntry(oldChannelName);
        if (oldChannel == null) {
            System.out.println("변경할 채팅방을 선택해주세요.");
            return;
        }
        oldChannel.setChatRoomName(newChannelName); //채널 객체의 필드를 setter를 통해 조작
        System.out.println("채팅방의 이름이 변경됐습니다. -> " + findChannelEntry(newChannelName));
    }

    @Override
    public void deleteChannelName(String channelName) {
        Channel channel = findChannelEntry(channelName);
        if (channel == null) {
            System.out.println("삭제할 채팅방을 선택해주세요.");
        }
        channelData.remove(findChannelEntry(channelName).getId());
        System.out.println("채팅방이 삭제되었습니다.");
    }

    public Channel getChannelById(UUID channelId) {
        Channel channel = channelData.values()
                .stream()
                .filter(v -> v.getId().equals(channelId))
                .findFirst()
                .orElse(null);
        return channel;
    }

    private Channel findChannelEntry(String channelName) {
        Channel channel = channelData.values() //Map<UUID, Channel>의 모든 Value(Channel)을 Collection<v> 형태로 반환
                .stream() // collection<v>에 java 연산 기능을 쓸 수 있도록 stream 형태로 변환 (이렇게 안 하면, for문을 돌려야 하나씩 찾아볼 수 있음.)(그래서 더 효율적인 stream API를 쓰는것.)
                .filter(v -> v.getChatRoomName().equals(channelName)) //중간 연산 : 원하는 조건만 남기는 filter(), 람다 표현식: v(ChatRoom 객체)의 getChatRoomName() 값이 channelName과 같은 경우만 유지
                .findFirst() // 최종 연산(반환): 남겨진 채팅방의 객체만 유지, 필터링 결과 중 첫번째 요소 반환
                .orElse(null); //Optional(최종 연산): 만약 없으면 null 반환
        return channel;
    }

    // Collection: 데이터를 메모리에 저장 (List, Set, Map 등)
    // Stream : 데이터를 저장하지 않고 흐름으로 처리 (한번만 사용 가능)
    // Stream API : Java에서 제공하는 함수형 연산 기능 (filter(), map(), reduce() 등)

    private boolean validateParameter (String channelName) {
        if (channelName.isEmpty()) {
            System.out.println("채팅방 이름은 한 글자 이상이 어야 합니다.");
            return false;
        }
        return true;
    }

}
