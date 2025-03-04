package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.util.Optional;

public class JavaApplication_FileIO {
    public static void main(String[] args) {
        FileUserRepository userRepository = new FileUserRepository();
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileMessageRepository messageRepository = new FileMessageRepository();

        // 사용자 저장 및 불러오기
        User user = new User("Jaeseok");
        userRepository.save(user);
        System.out.println("✅ User 저장 완료: " + user.getUserName());

        Optional<User> loadedUser = userRepository.findById(user.getId());
        System.out.println("📁 Loaded User: " + loadedUser.map(User::getUserName).orElse("User not found"));

        // 채널 저장 및 로드 테스트
        Channel channel = new Channel("Back-End Sprint 2");
        channelRepository.save(channel);
        System.out.println("✅ Channel 저장 완료: " + channel.getName());

        Optional<Channel> loadedChannel = channelRepository.findById(channel.getId());
        System.out.println("📁 Loaded Message: " + loadedChannel.map(Channel::getName).orElse("Channel not found"));
    }
}
