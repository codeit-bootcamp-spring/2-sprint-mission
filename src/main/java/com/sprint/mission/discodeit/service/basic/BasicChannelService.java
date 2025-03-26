package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.create.PrivateChannelCreateRequestDTO;
import com.sprint.mission.discodeit.dto.create.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.Empty.EmptyMessageListException;
import com.sprint.mission.discodeit.exception.NotFound.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.Valid.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.*;
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
    public Channel create(UUID userId, PublicChannelCreateRequestDTO channelCreateDTO) {
        User user = userRepository.findById(userId);
        Server findServer = serverRepository.findById(channelCreateDTO.serverId());
        Channel channel;

        channel = new Channel(findServer.getServerId(), user.getId(), channelCreateDTO.name());

        channelRepository.save(findServer, channel);
        channelRepository.join(channel, user);

        return channel;
    }

    @Override
    public Channel create(UUID userId, PrivateChannelCreateRequestDTO requestDTO) {
        Channel channel = new Channel(requestDTO.serverId(), userId, null);
        Server findServer = serverRepository.findById(requestDTO.serverId());
        Channel createdChannel = channelRepository.save(findServer, channel);

        requestDTO.participantIds().stream()
                .map(u -> new ReadStatus(u, createdChannel.getChannelId(), Instant.MIN))
                .forEach(readStatusRepository::save);

        return createdChannel;
    }

    @Override
    public void join(UUID channelId, UUID userId) {
        Channel findChannel = channelRepository.find(channelId);
        User user = userRepository.findById(userId);
        User join = channelRepository.join(findChannel, user);

//        if (findChannel.getType() == ChannelType.PRIVATE) {
//            createReadStatus(user, findChannel);
//        }
    }

    @CustomLogging
    @Override
    public void quit(UUID channelId, UUID userId) {
        Channel findChannel = channelRepository.find(channelId);
        User user = userRepository.findById(userId);

        User quit = channelRepository.quit(findChannel, user);
    }

    @Override
    public ChannelFindDTO find(UUID channelId) {
        Channel channel = channelRepository.find(channelId);
        Instant lastMessageTime = null;

        try {
            List<Message> messageList = messageRepository.findAllByChannelId(channelId);
            Message message = messageList.get(messageList.size() - 1);
            lastMessageTime = message.createdAt;
        } catch (EmptyMessageListException e) {

        } catch (MessageNotFoundException e) {

        }

        if (channel.getType() == ChannelType.PUBLIC) {
            return new ChannelFindDTO(channel.getChannelId(), channel.getName(), null, lastMessageTime);
        } else {
            List<User> userList = channel.getUserList();
            List<UUID> userIdList = userList.stream().map(User::getId).toList();
            return new ChannelFindDTO(channel.getChannelId(), channel.getName(), userIdList, lastMessageTime);
        }
    }

    @Override
    public List<ChannelFindDTO> findAllByServerAndUser(UUID serverId) {
        List<Channel> channelList = channelRepository.findAllByServerId(serverId);
        List<ChannelFindDTO> findDTOList = new ArrayList<>();

        for (Channel channel : channelList) {
            ChannelFindDTO channelFindDTO = find(channel.getChannelId());
            findDTOList.add(channelFindDTO);
        }
        return findDTOList;
    }

    @CustomLogging
    @Override
    public UUID update(UUID channelId, UpdateChannelDTO updateChannelDTO) {

        Channel findChannel = channelRepository.find(channelId);

        if (findChannel.getType() == ChannelType.PRIVATE) {
            throw new ChannelModificationNotAllowedException("private 채널은 수정할 수 없습니다.");
        }

        Channel update = channelRepository.update(findChannel, updateChannelDTO);
        return update.getChannelId();
    }


    @Override
    public void delete(UUID channelId) {

        channelRepository.remove(channelId);
        deleteAllMessage(channelId);
        deleteAllReadStatus(channelId);
    }

    @Override
    public void printChannels(UUID serverId) {

        List<Channel> channels = channelRepository.findAllByServerId(serverId);
        channels.forEach(System.out::println);

    }

    @Override
    public void printUsersInChannel(UUID channelId) {
        Channel channel = channelRepository.find(channelId);
        List<User> list = channel.getUserList();
        list.forEach(System.out::println);

    }

//
//    private void createReadStatus(User user, Channel channel) {
//        ReadStatus readStatus = new ReadStatus(user.getId(), channel.getChannelId());
//        readStatusRepository.save(readStatus);
//    }

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
