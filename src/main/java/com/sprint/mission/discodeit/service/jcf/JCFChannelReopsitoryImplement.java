package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

//채널이름,채널주인,리스트
public class JCFChannelReopsitoryImplement implements ChannelRepository {
    // 채널 데이터를 저장할 Map (채널이름/유저)
    private final Map<String,List <String>> channels = new HashMap<>();

    @Override
    public void createChannel(String channelName, String userId) {
        channels.putIfAbsent(channelName, new ArrayList<>()); //존재 x시 리스트 생성
        List<String> userList = channels.get(channelName);
        userList.add(userId);
        }

    @Override
    public List<String> AllChannelUserList() {
        return channels.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public List<String> getAllChannels() {
        return new ArrayList<>(channels.keySet());
    }

    public  boolean exsitChannel(String channelname){
        return channels.containsKey(channelname);
    }
    @Override
        public void updateChannel(String channelName, String newChannelName, String userId) {
            // 기존 채널의 유저 목록을 가져와서 채널 이름을 변경
            List<String> users = channels.remove(channelName);
            channels.put(newChannelName, users);
        }

    @Override
    public void deleteChannel(String channelId) {
        channels.remove(channelId);
    }

    @Override
    public void addUserToChannel(String channelName,String userId) {
        channels.get(channelName).add(userId);
    }

    @Override
    public void removeUserFromChannel(String channelId, String userId) {
        channels.get(channelId).remove(userId);

    }
    @Override
    public List<String> getChannelUserList(String channelName) {
        return channels.get(channelName);
    }//채널이 존재하는지 확인
    public boolean containsChannel(String channelId) {
       return channels.containsKey(channelId);
    }
    //소유권이 있는지 확인
    public boolean isChannelOwner(String channelName, String userId) {
       return channels.get(channelName).get(0).equals(userId);
    }
    }

