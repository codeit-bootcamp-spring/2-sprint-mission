package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    UserRepository userRepository;
    ChannelRepository channelRepository;

    public FileMessageRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void messageSave(UUID channelUUID, UUID userUUID, String content) {
        Message message = new Message(channelUUID, userUUID, content);

        try {
            String fileName = "message.ser";
            // 파일 존재 여부 확인
            boolean append = new File(fileName).exists();

            FileOutputStream fos = new FileOutputStream(fileName, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(message);

            oos.close();
            fos.close();

            System.out.println("[성공]" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
