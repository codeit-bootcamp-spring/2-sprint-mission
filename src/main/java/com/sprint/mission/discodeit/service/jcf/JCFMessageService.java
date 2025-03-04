package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.Repository.jcf.impl.LinkedListJCFUserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final Map<UUID, JCFUserRepository> userTable = new HashMap<>();

    private JCFMessageService() {
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    //레포지토리 생성
    private JCFUserRepository getUserRepository(UUID id) {
        JCFUserRepository JCFUserRepository = userTable.get(id);
        if (JCFUserRepository == null) {
            LinkedListJCFUserRepository repository = new LinkedListJCFUserRepository();
            userTable.put(id, repository);
            JCFUserRepository = repository;
        }
        return JCFUserRepository;
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
        JCFUserRepository myRepository = getUserRepository(myId);
        JCFUserRepository yourRepository = getUserRepository(targetId);
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
        JCFUserRepository myRepository = getUserRepository(myId);
        JCFUserRepository yourRepository = getUserRepository(targetId);
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
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        System.out.println("\n=========개인 메시지함==========");
        int i = 1;
        for (Message message : myMessages) {
            System.out.println(i++ + " : " + message.getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean remove(UUID myId, UUID targetId) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();

        return remove(myMessages, yourMessages, str);
    }

    @Override
    public boolean remove(UUID myId, UUID targetId, Message message) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        return remove(myMessages, yourMessages, message.getName());
    }

    @Override
    public boolean remove(UUID myId, UUID targetId, String str) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
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

    @Override
    public boolean update(UUID myId, UUID targetId) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        Scanner sc = new Scanner(System.in);

        System.out.print("바꿀려고 하는 대상의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();
        System.out.print("메시지를 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return update(myMessages, yourMessages, targetName, replaceName);
    }

    @Override
    public boolean update(UUID myId, UUID targetId, String targetName) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        Scanner sc = new Scanner(System.in);

        System.out.print("메시지를 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return update(myMessages, yourMessages, targetName, replaceName);
    }

    @Override
    public boolean update(UUID myId, UUID targetId, String targetName, String replaceName) {
        JCFUserRepository myRepository = getUserRepository(myId);
        Map<UUID, Queue<Message>> myMessageList = myRepository.getMessageList();
        Queue<Message> myMessages = getMessages(myMessageList, myId);

        JCFUserRepository yourRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> yourMessageList = yourRepository.getMessageList();
        Queue<Message> yourMessages = getMessages(yourMessageList, targetId);

        return update(myMessages, yourMessages, targetName, replaceName);
    }

    private boolean update(Queue<Message> myMessages, Queue<Message> yourMessages, String targetName, String replaceName) {
        boolean find = false;
        Message temp = null;
        for (Message message : myMessages) {
            if (message.getName().equals(targetName)) {
                find = true;
                temp = message;
                break;
            }
        }
        if (find) {
            for (Message message : yourMessages) {
                if (message.getName().equals(targetName)) {
                    temp.setName(replaceName);
                    message.setName(replaceName);
                    return true;
                }
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
