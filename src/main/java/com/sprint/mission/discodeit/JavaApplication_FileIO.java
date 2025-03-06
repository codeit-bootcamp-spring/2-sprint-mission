package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.RepositoryType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.util.List;
import java.util.Optional;

public class JavaApplication_FileIO {
    public static void main(String[] args) {
        UserRepository userRepository = RepositoryType.getUserRepository();
        ChannelRepository channelRepository = RepositoryType.getChannelRepository();
        MessageRepository messageRepository = RepositoryType.getMessageRepository();

        // 사용자 저장 및 불러오기
        User user = new User("Jaeseok");
        userRepository.save(user);
        System.out.println("✅ User 저장 완료: " + user.getUserName());

        Optional<User> loadedUser = userRepository.findById(user.getId());
        System.out.println("📁 Loaded User: " + loadedUser.map(User::getUserName).orElse("User not found"));

        // 채널 저장 로드 테스트
        Channel channel = new Channel("Back-End Sprint 2");
        channelRepository.save(channel);
        System.out.println("✅ Channel 저장 완료: " + channel.getName());

        Optional<Channel> loadedChannel = channelRepository.findById(channel.getId());
        System.out.println("📁 Loaded Channel: " + loadedChannel.map(Channel::getName).orElse("Channel not found"));

        // 메시지 저장 및 로드 테스트
        Message message = new Message("안녕하세요. 저는 허재석입니다.", user.getId(), channel.getId());
        messageRepository.save(message);
        System.out.println("✅ Message 저장 완료: " + message.getContent());

        Optional<Message> loadedMessage = messageRepository.findById(message.getId());
        System.out.println("📁 Loaded Message: " + loadedMessage.map(Message::getContent).orElse("Message not found"));

        // 전체 데이터 확인 (리스트 조회)
        List<User> allUsers = userRepository.findAll();
        System.out.println("\n 전체 User 목록");
        for (User user1 : allUsers) {
            System.out.println("-" + user1.getUserName());
        }

        List<Channel> allChannels = channelRepository.findAll();
        System.out.println("\n 전체 Channel 목록");
        for (Channel channel1 : allChannels) {
            System.out.println("-" + channel1.getName());
        }

        List<Message> allMessages = messageRepository.findAll();
        System.out.println("\n 전체 Message 목록");
        for (Message message1 : allMessages) {
            System.out.println("-" + message1.getContent());
        }
    }
}
