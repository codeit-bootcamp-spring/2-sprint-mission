package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    UserService userService;
    ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void sendMessage(UUID channelUUID, UUID userUUID, String content) {
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

    @Override
    public Message findMessageById(UUID messageUUID) {
        boolean fileCheck = new File("message.ser").exists();
        if (!fileCheck) {
            System.out.println("[실패] 메시지가 존재하지 않습니다.");
            return null;
        }

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    if (message.getId().equals(messageUUID)) return message;
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
        System.out.println("[실패] 메시지가 존재하지 않습니다.");
        return null;
    }

    @Override
    public Optional<List<Message>> findAllMessages() {
        List<Message> messages = new ArrayList<>();

        boolean fileCheck = new File("message.ser").exists();
        if (!fileCheck) return Optional.empty();

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    messages.add(message);
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
        return Optional.of(messages);
    }

    @Override
    public Optional<List<Message>> findMessageByChannelId(UUID channelUUID) {
        String fileName = "message.ser";
        // 파일 존재 여부 확인
        boolean append = new File(fileName).exists();
        if (!append) return Optional.empty();

        List<Message> messages = findAllMessages().orElse(Collections.emptyList());

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    if(message.getChannelUUID().equals(channelUUID)) messages.add(message);
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
        return Optional.of(messages);
    }

    @Override
    public void updateMessage(UUID messageUUID, String content) {
        List<Message> messages = findAllMessages().orElse(Collections.emptyList());

        messages.stream()
                .filter(message -> message.getId().equals(messageUUID))
                .findAny()
                .ifPresentOrElse(
                        message -> {
                            message.updateContent(content);
                            System.out.println("[성공]메세지 변경 완료" + message);
                        },
                        () -> System.out.println("[실패]수정하려는 메세지가 존재하지 않습니다"));

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messages) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessageById(UUID messageUUID) {
        List<Message> messages = findAllMessages().orElse(Collections.emptyList());

        boolean removed = messages.removeIf(message -> message.getId().equals(messageUUID));

        if (!removed) {
            System.out.println("[실패]메시지가 존재하지 않습니다");
            return;
        } else {
            System.out.println("[성공]메시지 삭제 완료");
        }

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messages) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
