package com.sprint.sprint1.mission.service;

import com.sprint.sprint1.mission.model.entity.ChannelTypes;
import com.sprint.sprint1.mission.model.entity.Message;
import com.sprint.sprint1.mission.model.entity.User;
import com.sprint.sprint1.mission.repository.UserRepository;
import com.sprint.sprint1.mission.view.input.MessageInput;
import com.sprint.sprint1.mission.view.output.MessageOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageService {


    private final UserRepository userRepository;
    private final MessageOutput messageOutput;
    List<Message> messages;

    public MessageService(UserRepository userRepository, MessageOutput messageOutput) {
        this.userRepository = userRepository;
        this.messageOutput = messageOutput;
        this.messages = new ArrayList<>();
    }

    public void validateUserAndStartChat(String email) {
        var byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            System.out.println("User " + email + "없는 회원 입니다. ");
            return;
        }
        User user = byEmail.get();
        messageOutput.checkOutput(user);
        startChat(user);
    }

    public void startChat(User user) {
        MessageInput messageInput = new MessageInput(this);
        messageInput.messageText(user.getEmail());
    }

    public void creatMessage(String message, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("해당 이메일의 사용자를 찾을 수 없습니다.");
            return;
        }
        User user = userOptional.get();
        Message newMessage = new Message(ChannelTypes.TEXT_CHANNEL.getDescription(),userOptional.get(), message);
        messages.add(newMessage);
        messageOutput.displayMessage(user.getUsername(), message);
    }

}
