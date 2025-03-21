package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Request.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.ChannelJoinQuitDTO;
import com.sprint.mission.discodeit.DTO.legacy.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.legacy.Channel.ChannelDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyMessageListException;
import com.sprint.mission.discodeit.Exception.NotFound.ChannelNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.MessageNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            channelRepository.reset();
        }
    }

    @CustomLogging
    @Override
    public Channel create(ChannelCreateDTO channelCreateDTO) {
        UUID serverId = channelCreateDTO.serverId();
        UUID creatorId = channelCreateDTO.creatorId();
        try {
            User user = userRepository.find(creatorId);
            Server findServer = serverRepository.find(serverId);
            Channel channel;

            if (channelCreateDTO.type() == ChannelType.PRIVATE) {
                channel = new Channel(findServer.getServerId(), user.getId(), null, ChannelType.PRIVATE);
                createReadStatus(user, channel);
            } else {
                channel = new Channel(findServer.getServerId(), user.getId(), channelCreateDTO.name());
            }

            channelRepository.save(findServer, channel);
            channelRepository.join(channel, user);

            return channel;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("create: 유저가 존재하지 않습니다.");
        } catch (ServerNotFoundException e) {
            throw new ServerNotFoundException("create: 서버가 존재하지 않습니다.");
        }
    }

    @Override
    public User join(ChannelJoinQuitDTO channelJoinQuitDTO) {
        Channel findChannel = channelRepository.find(channelJoinQuitDTO.channelId());
        User user = userRepository.find(channelJoinQuitDTO.userId());
        User join = channelRepository.join(findChannel, user);

        if (channelJoinQuitDTO.type() == ChannelType.PRIVATE) {
            createReadStatus(user, findChannel);
        }

        return join;
    }

    @CustomLogging
    @Override
    public User quit(ChannelJoinQuitDTO channelJoinQuitDTO) {

        Channel findChannel = channelRepository.find(channelJoinQuitDTO.channelId());
        User user = userRepository.find(channelJoinQuitDTO.userId());

        User quit = channelRepository.quit(findChannel, user);

        return quit;
    }

    @Override
    public ChannelDTO find(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        Channel channel = channelRepository.find(channelUUID);
        Instant lastMessageTime = null;

        try {
            List<Message> messageList = messageRepository.findAllByChannelId(channelUUID);
            Message message = messageList.get(messageList.size() - 1);
            lastMessageTime = message.createdAt;
        } catch (EmptyMessageListException e) {

        } catch (MessageNotFoundException e) {

        }

        if (channel.getType() == ChannelType.PUBLIC) {
            ChannelDTO channelDTO = ChannelDTO.find(channel.getChannelId(), channel.getName(), null, lastMessageTime);
            return channelDTO;
        } else {
            List<User> userList = channel.getUserList();
            List<UUID> userIdList = userList.stream().map(User::getId).toList();
            ChannelDTO channelDTO = ChannelDTO.find(channel.getChannelId(), channel.getName(), userIdList, lastMessageTime);
            return channelDTO;
        }
    }

    @Override
    public List<ChannelDTO> findAllByServerAndUser(String serverId) {
        UUID serverUUID = UUID.fromString(serverId);
        List<Channel> channelList = channelRepository.findAllByServerId(serverUUID);
        List<ChannelDTO> findDTOList = new ArrayList<>();

        for (Channel channel : channelList) {
            ChannelDTO channelFindDTO = find(channel.getChannelId().toString());
            findDTOList.add(channelFindDTO);
        }
        return findDTOList;
    }

    @Override
    public boolean delete(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);

        try {
            channelRepository.remove(channelUUID);
            deleteAllMessage(channelUUID);
            deleteAllReadStatus(channelUUID);
            return true;

        } catch (MessageNotFoundException e) {
            System.out.println("삭제 중 메시지 함이 없습니다.");
            return false;
        } catch (ServerNotFoundException e) {
            System.out.println("서버를 찾을 수 없습니다.");
            return false;
        } catch (ChannelNotFoundException e) {
            System.out.println("채널을 찾을 수 없습니다.");
            return false;
        }
    }

    @CustomLogging
    @Override
    public boolean update(ChannelCRUDDTO channelCRUDDTO, ChannelCRUDDTO channelUpdateDTO) {
        UUID userId = channelCRUDDTO.creatorId();
        UUID channelId = channelCRUDDTO.channelId();

        Channel findChannel = channelRepository.find(channelId);
        if (findChannel.getType() == ChannelType.PRIVATE) {
            System.out.println("private 채널은 수정할 수 없습니다.");
            return false;
        }

        if (findChannel.getCreatorId().equals(userId)) {
            channelRepository.update(findChannel, channelUpdateDTO);
            return true;
        } else {
            System.out.println("채널 수정 권한 없음");
            return false;
        }
    }

    @Override
    public boolean printChannels(String serverId) {
        UUID serverUUID = UUID.fromString(serverId);

        List<Channel> channels = channelRepository.findAllByServerId(serverUUID);
        channels.forEach(System.out::println);
        return true;
    }

    @Override
    public boolean printUsersInChannel(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        Channel channel = channelRepository.find(channelUUID);
        List<User> list = channel.getUserList();
        list.forEach(System.out::println);
        return true;
    }


    private void createReadStatus(User user, Channel channel) {
        ReadStatus readStatus = new ReadStatus(user.getId(), channel.getChannelId());
        readStatusRepository.save(readStatus);
    }

    private void deleteAllMessage(UUID channelId) {
        List<Message> list = messageRepository.findAllByChannelId(channelId);
        for (Message message : list) {
            messageRepository.remove(message.getMessageId());
        }
    }

    private void deleteAllReadStatus(UUID channelId) {
        List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
        for (ReadStatus status : readStatusList) {
            readStatusRepository.delete(status.getReadStatusId());
        }
    }
}
