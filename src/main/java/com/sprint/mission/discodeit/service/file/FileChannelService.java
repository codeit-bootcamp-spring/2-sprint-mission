package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//@Service
//@RequiredArgsConstructor
//public class FileChannelService implements ChannelService {
//    private final FileUserRepository userRepository;
//    private final FileChannelRepository serverRepository;
//
//
//    @Override
//    public void reset(boolean adminAuth) {
//        if (adminAuth == true) {
//            serverRepository.reset();
//        }
//    }
//
//    @Override
//    public UUID createChannel(String serverId, String creatorId, String name) {
//        UUID SID = UUID.fromString(serverId);
//        UUID UID = UUID.fromString(creatorId);
//
//        User user = userRepository.findUserByUserId(UID);
//        System.out.println(JCFUserRepository.class + " : " + "createChannel" + " : " + user.getId());
//        Server findServer = userRepository.findServerByServerId(user, SID);
//        System.out.println(JCFUserRepository.class + " : " + "createChannel" + " : " + findServer.getServerId());
//
//        Channel channel = new Channel(findServer.getServerId(), user.getId(), name);
//        serverRepository.save(findServer, channel);
//
//        return channel.getChannelId();
//    }
//
//
//    @Override
//    public UUID joinChannel(String serverId, String userId, String ownerId,String channelId) {
//        UUID SID = UUID.fromString(serverId);
//        UUID UID = UUID.fromString(userId);
//        UUID UOID = UUID.fromString(ownerId);
//        UUID CID = UUID.fromString(channelId);
//
//        User user = userRepository.findUserByUserId(UID);
//        User owner = userRepository.findUserByUserId(UOID);
//        Server findServer = userRepository.findServerByServerId(owner, SID);
//        Channel findChannel = serverRepository.findChannelByChanelId(findServer, CID);
//
//        UUID uuid = serverRepository.join(findChannel, user);
//
//        return uuid;
//    }
//
//    @Override
//    public UUID quitChannel(String serverId, String userId, String channelId) {
//        UUID SID = UUID.fromString(serverId);
//        UUID UID = UUID.fromString(userId);
//        UUID CID = UUID.fromString(channelId);
//
//        User user = userRepository.findUserByUserId(UID);
//        Server findServer = userRepository.findServerByServerId(user, SID);
//        Channel findChannel = serverRepository.findChannelByChanelId(findServer, CID);
//
//        UUID uuid = serverRepository.quit(findChannel, user);
//
//        return uuid;
//    }
//
//    @Override
//    public boolean printUsers(String serverId) {
//        UUID SID = UUID.fromString(serverId);
//
//        List<Channel> channels = serverRepository.findChannelListByServerId(SID);
//
//        for (Channel channel : channels) {
//            List<User> users = serverRepository.findUserListByChannelId(channel.getServerId());
//            System.out.println(channel.getName());
//            for (User user : users) {
//                System.out.println(user);
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean printChannels(String serverId) {
//        UUID SID = UUID.fromString(serverId);
//
//        List<Channel> channels = serverRepository.findChannelListByServerId(SID);
//        for (Channel channel : channels) {
//            System.out.println(channel);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean printUsersInChannel(String channelId) {
//        UUID CID = UUID.fromString(channelId);
//
//        List<User> users = serverRepository.findUserListByChannelId(CID);
//        for (User user : users) {
//            System.out.println(user);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean removeChannel(String serverId, String creatorId, String channelId) {
//        UUID SID = UUID.fromString(serverId);
//        UUID UID = UUID.fromString(creatorId);
//        UUID CID = UUID.fromString(channelId);
//
//        User user = userRepository.findUserByUserId(UID);
//        Server findServer = userRepository.findServerByServerId(user, SID);
//        Channel findChannel = serverRepository.findChannelByChanelId(findServer, CID);
//
//        if (findChannel.getCreatorId().equals(UID)) {
//            serverRepository.remove(findServer, findChannel);
//            return true;
//        } else {
//            System.out.println("채널 삭제 권한 없음");
//            return false;
//        }
//    }
//
//    @Override
//    public boolean updateChannelName(String serverId, String creatorId, String channelId, String replaceName) {
//        UUID SID = UUID.fromString(serverId);
//        UUID UID = UUID.fromString(creatorId);
//        UUID CID = UUID.fromString(channelId);
//
//        User user = userRepository.findUserByUserId(UID);
//        Server findServer = userRepository.findServerByServerId(user, SID);
//        Channel findChannel = serverRepository.findChannelByChanelId(findServer, CID);
//
//        if (findChannel.getCreatorId().equals(UID)) {
//            serverRepository.update(findServer, findChannel, replaceName);
//            return true;
//        } else {
//            System.out.println("채널 수정 권한 없음");
//            return false;
//        }
//    }
//}
