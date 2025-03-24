//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//public class FileMessageService implements MessageService {
//
//    private static FileMessageService INSTANCE;
//    private final UserService userService;
//    private final ChannelService channelService;
//    private final FileMessageRepository fileMessageRepository;
//
//    private FileMessageService(UserService userService, ChannelService channelService, FileMessageRepository fileMessageRepository) {
//        this.fileMessageRepository = fileMessageRepository;
//        this.userService = userService;
//        this.channelService = channelService;
//    }
//
//    public static synchronized FileMessageService getInstance(UserService userService, ChannelService channelService, FileMessageRepository fileMessageRepository) {
//        if (INSTANCE == null) {
//            INSTANCE = new FileMessageService(userService, channelService, fileMessageRepository);
//        }
//        return INSTANCE;
//    }
//
//    private void saveMessage(){
//        fileMessageRepository.save();
//    }
//
//    @Override
//    public Message createMessage(UUID senderId, UUID channelId, String content) {
//        userService.validateUserExists(senderId);
//        channelService.validateChannelExists(channelId);
//
//        if(!channelService.findChannelById(channelId).getMembers().contains(senderId)){
//            throw new IllegalStateException("유저가 채널에 속해있지 않음.");
//        }
//
//        Message message = new Message(senderId, channelId, content);
//        channelService.addMessage(channelId, message.getId());
//        fileMessageRepository.addMessage(message);
//        return message;
//    }
//
//    @Override
//    public Message getMessageById(UUID messageId) {
//        validateMessageExists(messageId);
//        return fileMessageRepository.findMessageById(messageId);
//    }
//
//    @Override
//    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
//        return fileMessageRepository.findMessageAll().stream()
//                .filter(message -> message.getChannelId().equals(channelId))
//                .filter(message -> message.getSenderId().equals(senderId))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Message> findChannelMessages(UUID channelId) {
//        return fileMessageRepository.findMessageAll().stream()
//                .filter(message -> message.getChannelId().equals(channelId))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Message> findUserMessages(UUID senderId) {
//        return fileMessageRepository.findMessageAll().stream()
//                .filter(message -> message.getSenderId().equals(senderId))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void updateMessage(UUID messageId, String newContent) {
//        validateMessageExists(messageId);
//        Message message = fileMessageRepository.findMessageById(messageId);
//        message.updateContent(newContent);
//        saveMessage();
//    }
//
//    @Override
//    public void deleteMessage(UUID messageId) {
//        validateMessageExists(messageId);
//
//        Message message = fileMessageRepository.findMessageById(messageId);
//        channelService.removeMessage(message.getChannelId(), messageId);
//        fileMessageRepository.deleteMessageById(messageId);
//    }
//
//    @Override
//    public void validateMessageExists(UUID messageId) {
//        if (!fileMessageRepository.existsById(messageId)) {
//            throw new NoSuchElementException("존재하지 않는 메세지입니다.");
//        }
//    }
//}
