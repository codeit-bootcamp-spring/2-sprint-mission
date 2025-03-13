package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final static File FILE = new File("channel.ser");

    @Override
    public Channel channelSave(String channelName) {
        Channel channel = new Channel(channelName);
        try {
            boolean append = FILE.exists();

            FileOutputStream fos = new FileOutputStream(FILE, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(channel);

            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelUUID) {
        if(!FILE.exists()){
            return Optional.empty();
        }

        try (FileInputStream fis = new FileInputStream(FILE);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Channel channel = (Channel) ois.readObject();
                    if (channel.getId().equals(channelUUID)) return Optional.of(channel);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = new ArrayList<>();

        if(!FILE.exists()){
            return channelList;
        }

        try (FileInputStream fis = new FileInputStream(FILE);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Channel channel = (Channel) ois.readObject();
                    channelList.add(channel);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return channelList;
    }

    @Override
    public Channel updateChannelChannelName(UUID channelUUID, String channelName) {
        if(!FILE.exists()){
            return null;
        }

        List<Channel> channels = findAllChannel();

        Channel updateChannel = channels.stream()
                .filter(channel -> channel.getId().equals(channelUUID))
                .findAny()
                .map(user -> {
                    user.updateChannelName(channelName);
                    return user;
                })
                .orElse(null);

        try (FileOutputStream fos = new FileOutputStream(FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Channel channel : channels) {
                oos.writeObject(channel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return updateChannel;
    }

    @Override
    public boolean deleteChannelById(UUID channelUUID) {
        List<Channel> channelList = findAllChannel();

        boolean removed = channelList.removeIf(channel -> channel.getId().equals(channelUUID));

        if (!removed) return false;

        try (FileOutputStream fos = new FileOutputStream(FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Channel channel : channelList) {
                oos.writeObject(channel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
