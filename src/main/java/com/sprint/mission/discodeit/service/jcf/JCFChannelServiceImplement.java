package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.*;

public class JCFChannelServiceImplement implements ChannelService {
    private final ChannelRepository channelRepo;
    private final UserRepository userRepo;
    public JCFChannelServiceImplement(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepo = channelRepository;
        this.userRepo = userRepository;
    }

    @Override
    public void getChannelList() {
        List <String> userList= channelRepo.AllChannelUserList();
        System.out.println("전체 채널 리스트#####");
        userList.stream().forEach(System.out::println);
        System.out.println("###################");
    }

    @Override
    public void updateChannel(String channelName, String newChannelName, String owner) {
        if (channelRepo.isChannelOwner(channelName, owner)) {
            // 변경 전 채널에 가입한 유저 리스트 가져오기
            List<String> userIds = channelRepo.getChannelUserList(channelName);
            // 채널 이름 업데이트
            channelRepo.updateChannel(channelName, newChannelName, owner);
            System.out.println("채널 이름 변경 완료: " + channelName + " -> " + newChannelName);

            // 변경 전 채널 이름을 소속 정보에서 제거하고, 새 채널 이름 추가
            for (String userId : userIds) {
                User user = userRepo.readuser(userId);
                if (userRepo.containsUser(userId)) {
                    user.removeBelongChannel(channelName);
                    user.addBelongChannel(newChannelName);
                    userRepo.updateUser(user.getuserId());  // 변경된 유저 정보 저장
                }
            }
            return;
        }
        System.out.println("권한이 없습니다.");
    }
    @Override
    public void getChannelUserList(String channelId) {
       List<String> user_channel_list= channelRepo.getChannelUserList(channelId);
        System.out.println(channelId+ "의 유저 리스트#####");
       user_channel_list.stream().forEach(System.out::println);
        System.out.println("###################");

    }

    @Override
    public void createChannel(String channelName, String userId) {
        if (channelRepo.containsChannel(channelName)){
            System.out.println(channelName+": 이미 존재합니다.");
            return;

        }
        if (userRepo.containsUser(userId)) {
            channelRepo.createChannel(channelName, userId);
            User user = userRepo.updateUser(userId);
            user.addBelongChannel(channelName);
            userRepo.updateUser(userId);
            channelRepo.isChannelOwner(channelName, userId);
            System.out.println(channelName + "채널이 생성되었습니다.");

        }
    }

    @Override
    public void joinChannel(String channelId, String userId) {

        channelRepo.addUserToChannel(channelId, userId);
        System.out.println(channelId+" : 채널에 가입되었습니다.");

    }
    @Override
    public void leaveChannel(String channelId, String userId) {
        if(userRepo.containsUser(userId) && !channelRepo.isChannelOwner(channelId, userId)) {
            channelRepo.removeUserFromChannel(channelId, userId);
            User user = userRepo.updateUser(userId); //업데이트
            user.removeBelongChannel(channelId); // 삭제
            channelRepo.removeUserFromChannel(channelId, userId);
            System.out.println("유저 " + userId + "가 채널 " + channelId + "에서 탈퇴하였습니다.");

        } else if (channelRepo.isChannelOwner(channelId, userId)) {
            System.out.println("오너 유저는 탈퇴할 수 없습니다");
            //권한 양도를 하면 탈퇴가능하도록 수정


        }
    }
}
