//package com.sprint.mission.discodeit.event;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelRepository;
//import com.sprint.mission.discodeit.service.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class ChannelDeletedEventListener {
//    private final UserRepository userRepository;
//    private final ChannelRepository channelRepository;
//
//    @EventListener
//    public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
//        Channel channel = channelRepository.findById(event.getChannelId()).orElse(null);
//        if (channel == null) return;
//        List<UUID> userIds = channel.getUserList().stream().toList();
//        channelRepository.deleteChannel(event.getChannelId());
//        // 모든 사용자의 채널 목록에서 해당 채널 제거
//        for (UUID userId : userIds) {
//            userRepository.findByUser(userId)
//                    .ifPresent(user -> user.removeBelongChannel(event.getChannelId()));
//        }
//    }
//}