//package com.sprint.mission.discodeit.event;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.service.*;
//import lombok.RequiredArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.context.event.EventListener;
////import org.springframework.stereotype.Component;
////
////import java.util.List;
////import java.util.Objects;
////import java.util.Set;
////import java.util.UUID;
////import java.util.stream.Collectors;
////
////
////public class ChannelEventListener {
////    private final UserRepository userRepository;
////    private final ChannelRepository channelRepository;
////    private final MessageRepository messageRepository;
////    private final BinaryContentRepository binaryContentRepository;
////    private final ReadStatusRepository readStatusRepository;
////
////    @EventListener
////    public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
////        UUID channelId = event.getChannelId();
////        Channel channel=channelRepository.findChannelById(channelId).orElse(null);
////        List<UUID> memberUserIds = Objects.requireNonNull(channel).getUserList().stream().toList();
////
////        log.info("채널 삭제 이벤트 처리: {}", channelId);
////
////        // 채널 멤버 사용자의 채널 목록에서 제거
////        for (UUID userId : memberUserIds) {
////            userRepository.findByUser(userId)
////                    .ifPresent(user -> {
////                        user.removeBelongChannel(channelId);
////                        userRepository.updateUser(user);
////                        log.debug("사용자 {}의 채널 목록에서 채널 {} 제거", userId, channelId);
////                    });
////        }
////
////        // 채널 관련 메시지 삭제
////        List<Message> channelMessages = messageRepository.findAll().stream()
////                .filter(msg -> msg.getChannelId().equals(channelId))
////                .toList();
////
////        for (Message message : channelMessages) {
////            messageRepository.deleteMessage(message.getId());
////            log.debug("채널 {} 관련 메시지 {} 삭제", channelId, message.getId());
////        }
////
////        // 채널 관련 바이너리 콘텐츠 삭제
////        boolean binaryDeleted = binaryContentRepository.delete(channelId);
////        log.debug("채널 {} 관련 바이너리 콘텐츠 삭제 결과: {}", channelId, binaryDeleted);
////
////        // 채널 관련 읽음 상태 삭제
////        List<ReadStatus> channelReadStatuses = readStatusRepository.findAllByUserId(null).stream()
////                .filter(status -> status.getChannelId().equals(channelId))
////                .toList();
////
////        for (ReadStatus status : channelReadStatuses) {
////            readStatusRepository.deleteReadStatus(status.getId());
////            log.debug("채널 {} 관련 읽음 상태 {} 삭제", channelId, status.getId());
////        }
////
////        // 채널 삭제 완료
////        log.info("채널 {} 및 관련 데이터 삭제 완료", channelId);
////    }
////
////    @EventListener
////    public void handleUserJoinedChannelEvent(UserJoinedChannelEvent event) {
////        UUID userId = event.getUserId();
////        UUID channelId = event.getChannelId();
////
////        log.info("사용자 {} 채널 {} 참여 이벤트 처리", userId, channelId);
////
////        // 채널에 사용자 추가
////        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
////        if (channel != null) {
////            channel.joinChannel(userId);
////            channelRepository.updateChannel(channel);
////            log.debug("채널 {}에 사용자 {} 추가 완료", channelId, userId);
////        }
////
////        // 사용자의 채널 목록에 추가
////        User user = userRepository.findByUser(userId).orElse(null);
////        if (user != null) {
////            user.addBelongChannel(channelId);
////            userRepository.updateUser(user);
////            log.debug("사용자 {}의 채널 목록에 채널 {} 추가 완료", userId, channelId);
////        }
////    }
////
////    @EventListener
////    public void handleUserLeftChannelEvent(UserLeftChannelEvent event) {
////        UUID userId = event.getUserId();
////        UUID channelId = event.getChannelId();
////
////        log.info("사용자 {} 채널 {} 탈퇴 이벤트 처리", userId, channelId);
////
////        // 채널에서 사용자 제거
////        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
////        if (channel != null) {
////            channel.leaveChannel(userId);
////            channelRepository.updateChannel(channel);
////            log.debug("채널 {}에서 사용자 {} 제거 완료", channelId, userId);
////        }
////
////        // 사용자의 채널 목록에서 제거
////        User user = userRepository.findByUser(userId).orElse(null);
////        if (user != null) {
////            user.removeBelongChannel(channelId);
////            userRepository.updateUser(user);
////            log.debug("사용자 {}의 채널 목록에서 채널 {} 제거 완료", userId, channelId);
////        }
////    }
////}
