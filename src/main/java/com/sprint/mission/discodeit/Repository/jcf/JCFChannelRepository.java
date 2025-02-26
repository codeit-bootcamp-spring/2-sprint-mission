package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.RepositoryService;

import java.util.List;
import java.util.Scanner;

public class JCFChannelRepository implements RepositoryService<Channel, Message> {
    private static JCFChannelRepository instance;
    private Message headMessage;

    private JCFChannelRepository() {

    }

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    @Override
    public Message getHead() {
        return headMessage;
    }

    @Override
    public List<Message> repository(Channel channel) {
        ChannelRepository channelRepository = channel.getChannelRepository();
        return channelRepository.getMessageList();
    }

    @Override
    public void add(Channel channel, Message message) {
        List<Message> repository = repository(channel);
        repository.add(message);
        System.out.println("메시지 추가 성공");
    }

    @Override
    public void remove(Channel channel) {
        List<Message> list = print(channel);
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 메시지의 이름을 입력하시오. : ");
        String s = sc.next();
        sc.close();
        for (Message data : list) {
            if (data.getStr().equals(s)) {
                list.remove(data);
                System.out.println("메시지 삭제 성공");
                return;
            }
        }
        System.out.println("해당 메시지가 존재하지 않습니다.");
    }

    @Override
    public List<Message> print(Channel channel) {
        List<Message> list = repository(channel);
        if (list.isEmpty()) {
            return null;
        }
        System.out.println("======================================");
        for (Message data : list) {
            System.out.println(data.getStr());
        }
        System.out.println("======================================");

        return list;
    }

    @Override
    public void update(Channel channel) {
        List<Message> list = print(channel);

        Scanner sc = new Scanner(System.in);
        System.out.print("업데이트할 메시지를 입력하시오. : ");
        String s = sc.next();
//        s.replaceAll("\n", "");
        for (Message data : list) {
            if (data.getStr().equals(s)) {
                System.out.print("\n바꿀 텍스트를 입력하시오. : ");
                s = sc.next();
//                s.replaceAll("\n", "");
                data.setStr(s);
                System.out.println("메시지 업데이트 성공");
                return;
            }
        }
//        sc.close();
        System.out.println("해당 메시지가 존재하지 않습니다.");
    }

    @Override
    public void search(Channel channel) {

    }
}
