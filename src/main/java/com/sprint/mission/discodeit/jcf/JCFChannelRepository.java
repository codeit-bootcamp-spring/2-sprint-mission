package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Repository.ChannelRepository;
import com.sprint.mission.discodeit.service.RepositoryService;

import java.util.List;
import java.util.Scanner;

public class JCFChannelRepository implements RepositoryService<Channel, Message> {
    private static JCFChannelRepository instance;

    private JCFChannelRepository() {

    }

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
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
        String s = sc.nextLine();
        sc.close();
        for (Message data : list) {
            if (data.getName().equals(s)) {
                list.remove(data);
                System.out.println("\n메시지 삭제 성공");
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

        for (Message data : list) {
            System.out.println(data.getName());
        }

        return list;    }

    @Override
    public void update(Channel channel) {
        List<Message> list = print(channel);

        Scanner sc = new Scanner(System.in);
        System.out.print("업데이트할 메시지의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Message data : list) {
            if (data.getName().equals(s)) {
                System.out.print("\n바꿀 이름을 입력하시오. : ");
                String b = sc.nextLine();
                data.setName(b);
                System.out.println("메시지 업데이트 성공");
                return;
            }
        }
        sc.close();
        System.out.println("해당 메시지가 존재하지 않습니다.");
    }

    @Override
    public void search(Channel channel) {

    }
}
