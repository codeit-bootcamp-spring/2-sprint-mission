package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
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
    public void sendMessage(SaveMessageParamDto saveMessageParamDto) {
        //Message message = new Message(
        //        saveMessageParamDto.content(),
        //        saveMessageParamDto.UserId(),
        //        saveMessageParamDto.channelId(),
        //
        //);
        //
        //try {
        //    String fileName = "message.ser";
        //    // 파일 존재 여부 확인
        //    boolean append = new File(fileName).exists();
        //
        //    FileOutputStream fos = new FileOutputStream(fileName, true);
        //    ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
        //    oos.writeObject(message);
        //
        //    oos.close();
        //    fos.close();
        //
        //    System.out.println("[성공]" + message);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
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
    public List<Message> findAllMessages() {
        List<Message> messageList = new ArrayList<>();

        boolean fileCheck = new File("message.ser").exists();

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    messageList.add(message);
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
        return messageList;
    }

    @Override
    public List<Message> findMessageByChannelId(UUID channelUUID) {
        String fileName = "message.ser";

        List<Message> messageList = findAllMessages();

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    if (message.getChannelUUID().equals(channelUUID)) messageList.add(message);
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
        return messageList;
    }

    @Override
    public void updateMessage(UpdateMessageParamDto updateMessageParamDto) {
        List<Message> messageList = findAllMessages();

        messageList.stream()
                .filter(message -> message.getId().equals(updateMessageParamDto.messageUUID()))
                .findAny()
                .ifPresentOrElse(
                        message -> {
                            message.updateContent(updateMessageParamDto.content());
                            System.out.println("[성공]메세지 변경 완료" + message);
                        },
                        () -> System.out.println("[실패]수정하려는 메세지가 존재하지 않습니다"));

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messageList) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessageById(UUID messageUUID) {
        List<Message> messageList = findAllMessages();

        boolean removed = messageList.removeIf(message -> message.getId().equals(messageUUID));

        if (!removed) {
            System.out.println("[실패]메시지가 존재하지 않습니다");
            return;
        } else {
            System.out.println("[성공]메시지 삭제 완료");
        }

        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Message message : messageList) {
                oos.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
