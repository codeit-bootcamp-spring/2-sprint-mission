package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileChannelRepository implements ChannelRepository {

    @Override
    public void channelSave(String channelName) {
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
}
