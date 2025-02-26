package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListUserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    JCFUserService userService = JCFUserService.getInstance();
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    private JCFMessageService() {
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {
            LinkedListUserRepository repository = new LinkedListUserRepository();
            userTable.put(id, repository);
            userRepository = repository;
        }
        return userRepository;
    }

    private Queue<Message> getMessages(Map<UUID, Queue<Message>> messageList, UUID id) {
        Queue<Message> messages = messageList.get(id);
        if (messages == null) {
            messages = new LinkedList<>();
            messageList.put(id, messages);
        }
        return messages;
    }

    @Override
    public void send(UUID myId, UUID targetId, String str) {
        //메시지 작성
        Message message = new Message(str);
        //메시지 전송 과정 시작
        System.out.println("전송중");
        //두 아이디의 유저 레포지토리 열기
        UserRepository myRepository = getUserRepository(myId);
        UserRepository yourRepository = getUserRepository(targetId);
        //유저 레포지토리 내 메시지함 가져오기
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        //메시지함 뜯기
        Queue<Message> myMessages = getMessages(myMessageList, myId);
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);
        //메시지 전송 및 주입
        myMessages.add(message);
        yourMessages.add(message);
        System.out.println("전송완료");
    }

    @Override
    public void send(UUID myId, UUID targetId, Message message) {
        //메시지 전송 과정 시작
        System.out.println("전송중");
        //두 아이디의 유저 레포지토리 열기
        UserRepository myRepository = getUserRepository(myId);
        UserRepository yourRepository = getUserRepository(targetId);
        //유저 레포지토리 내 메시지함 가져오기
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        //메시지함 뜯기
        Queue<Message> myMessages = getMessages(myMessageList, myId);
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);
        //내 메시지함에 메시지 주입
        myMessages.add(message);
        //상대방 메시지함에 메시지 주입
        yourMessages.add(message);
        System.out.println("전송완료");
    }

    @Override
    public void read(UUID myId) {
        UserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);
        if (myMessageList == null) {
            System.out.println("비어있습니다.");
            return;
        }

        System.out.println("=========메시지 목록==========");
        int i = 1;
        for (Message message : myMessages) {
            System.out.println(i++ + " : " + message.getName());
        }
        System.out.println("=========================");
    }

    @Override
    public boolean remove(UUID myId, UUID targetId) {
        UserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        UserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();

        return remove(myMessages, yourMessages, str);
    }

    @Override
    public boolean remove(UUID myId, UUID targetId, Message message) {
        UserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        UserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        return remove(myMessages, yourMessages, message.getName());
    }

    @Override
    public boolean remove(UUID myId, UUID targetId, String str) {
        UserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        UserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        return remove(myMessages, yourMessages, str);
    }

    private boolean remove(Queue<Message> myMessages, Queue<Message> yourMessages, String str) {
        boolean find = false;
        for (Message message : myMessages) {
            if (message.getName().equals(str)) {
                find = true;
                break;
            }
        }
        if (find) {
            for (Message message : yourMessages) {
                if (message.getName().equals(str)) {
                    myMessages.remove(message);
                    yourMessages.remove(message);
                    return true;
                }
            }

        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
