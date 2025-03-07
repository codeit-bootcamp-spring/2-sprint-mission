package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    @Override
    public Message messageSave(UUID channelUUID, UUID userUUID, String content) {
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

            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Message> findMessageById(UUID messageUUID) {
        boolean fileCheck = new File("message.ser").exists();
        if (!fileCheck) {
            return Optional.empty();
        }

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    if (message.getId().equals(messageUUID)) return Optional.of(message);
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
    public List<Message> findAllMessage() {
        List<Message> messages = new ArrayList<>();

        boolean fileCheck = new File("message.ser").exists();
        if (!fileCheck) return null;

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    messages.add(message);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

    @Override
    public List<Message> findMessageByChannel(UUID channelUUID) {
        String fileName = "message.ser";

        boolean append = new File(fileName).exists();
        if (!append) return null;

        List<Message> messages = findAllMessage();

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    if(message.getChannelUUID().equals(channelUUID)) messages.add(message);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

    @Override
    public Message updateMessage(UUID messageUUID, String content) {
        List<Message> messages = findAllMessage();

        Message updateContent = messages.stream()
                .filter(message -> message.getId().equals(messageUUID))
                .findAny()
                .map(message ->  {
                    message.updateContent(content);
                    return message;
                })
                .orElse(null);

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messages) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateContent;
    }

    @Override
    public boolean deleteMessageById(UUID messageUUID) {
        List<Message> messages = findAllMessage();

        boolean removed = messages.removeIf(message -> message.getId().equals(messageUUID));

        if (!removed) return false;

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messages) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
