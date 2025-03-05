package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    @Override
    public void createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        try {
            String fileName = "channel.ser";
            // 파일 존재 여부 확인
            boolean append = new File(fileName).exists();

            FileOutputStream fos = new FileOutputStream(fileName, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(channel);

            oos.close();
            fos.close();

            System.out.println("[성공]" + channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel findChannel(UUID channelUUID) {
        try (FileInputStream fis = new FileInputStream("channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Channel channel = (Channel) ois.readObject();
                    if (channel.getId().equals(channelUUID)) return channel;
                } catch (EOFException e) {
                    // 파일의 끝 도달 시 브레이크
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[실패] 채널이 존재하지 않습니다.");
        return null;
    }

    @Override
    public Optional<List<Channel>> findAllChannel() {
        List<Channel> channels = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Channel channel = (Channel) ois.readObject();
                    channels.add(channel);
                } catch (EOFException e) {
                    // 파일의 끝 도달 시 브레이크
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(channels);
    }

    @Override
    public void updateChannel(UUID channelUUID, String channelName) {
        List<Channel> channels = findAllChannel().orElse(Collections.emptyList());

        channels.stream()
                .filter(channel -> channel.getId().equals(channelUUID))
                .findAny()
                .ifPresentOrElse(
                        channel -> {
                            channel.updateChannelName(channelName);
                            System.out.println("[성공] 채널 변경 완료" + channel);
                        },
                        () -> System.out.println("[실패]수정하려는 채널이 존재하지 않습니다"));

        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Channel channel : channels) {
                oos.writeObject(channel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteChannel(UUID channelUUId) {
        List<Channel> channels = findAllChannel().orElse(Collections.emptyList());

        boolean removed = channels.removeIf(channel -> channel.getId().equals(channelUUId));

        if (!removed) {
            System.out.println("[실패]존재하지 않는 채널");
            return;
        } else {
            System.out.println("[성공]채널 삭제 완료");
        }

        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Channel channel : channels) {
                oos.writeObject(channel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
