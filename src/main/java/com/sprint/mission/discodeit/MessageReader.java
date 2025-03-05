package com.sprint.mission.discodeit;
import com.sprint.mission.discodeit.entity.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.UUID;

public class MessageReader {
    public static void main(String[] args) {
        String filename = "messages.ser";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Map<UUID, Message> messages = (Map<UUID, Message>) ois.readObject();
            for (Map.Entry<UUID, Message> entry : messages.entrySet()) {
                System.out.println("UUID: " + entry.getKey());
                System.out.println("Message: " + entry.getValue());
                System.out.println("-----------------------");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
