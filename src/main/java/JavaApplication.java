import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.DiscordService;
import com.sprint.mission.discodeit.FrontEnd.Repository.FileDiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.Repository.JCFDiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.Service.FileDiscordService;
import com.sprint.mission.discodeit.Repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.Repository.file.FileServerRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicDiscordService;
import com.sprint.mission.discodeit.service.basic.BasicServerService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileServerService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Scanner;

public class JavaApplication {
    public static Server serverEngine(UserService userService, User user) {
        //서버 생성
        Server server1 = userService.createServer("Server1");
        Server server2 = userService.createServer("Server2");
        Server server3 = userService.createServer("Server3");

        //서버 등록
        userService.addServer(user.getId(), server1);
        userService.addServer(user.getId(), server2);
        userService.addServer(user.getId(), server3);

        //서버 불러오기
        server1 = userService.getServer(user.getId(), "Server1");
        server2 = userService.getServer(user.getId(), "Server2");
        server3 = userService.getServer(user.getId(), "Server3");

        //유저가 가지고 있는 서버 조회
        userService.printServer(user.getId());


        // 특정 데이터 조회
        Server getServer = userService.getServer(user.getId(), "Server1");
        System.out.println("getServer = \n" + getServer);

        // 서버 수정
        userService.updateServer(user.getId(), "Server3", "replaceServer");
        userService.printServer(user.getId());

        //서버명 복원
        userService.updateServer(user.getId(), "replaceServer", "Server3");
        userService.printServer(user.getId());

        //서버 삭제
        userService.removeServer(user.getId(), "Server3");
        userService.printServer(user.getId());

        return server1;

    }

    public static Channel channelEngine(ServerService serverService, Server server1) {
        // 채널 생성
        Channel channel1 = serverService.createChannel("Channel1");
        Channel channel2 = serverService.createChannel("Channel2");
        Channel channel3 = serverService.createChannel("Channel3");

        //채널 등록
        serverService.addChannel(server1.getId(),channel1);
        serverService.addChannel(server1.getId(),channel2);
        serverService.addChannel(server1.getId(),channel3);

        //채널 불러오기
        channel1 = serverService.getChannel(server1.getId(), "Channel1");
        channel2 = serverService.getChannel(server1.getId(), "Channel2");
       channel3 = serverService.getChannel(server1.getId(), "Channel3");

        // 모든 채널 조회
        serverService.printChannel(server1.getId());

        // 특정 채널 조회
        Channel getChannel = serverService.getChannel(server1.getId(), "Channel1");
        System.out.println("getChannel = \n" + getChannel);

        // 채널명 수정
        serverService.updateChannel(server1.getId(), "Channel3", "replaceChannel");
        serverService.printChannel(server1.getId());

        serverService.updateChannel(server1.getId(), "replaceChannel", "Channel3");
        serverService.printChannel(server1.getId());

        // 채널 삭제
        serverService.removeChannel(server1.getId(), "Channel3");
        serverService.printChannel(server1.getId());

        return channel1;
    }

    public static void messageEngine(ChannelService channelService, Channel channel1) {
        // 채널 내 메시지 생성 및 저장
        channelService.write(channel1.getId(), "hello");
        channelService.write(channel1.getId(), "world");
        channelService.write(channel1.getId(), "nice");

        //저장된 특정 메시지 조회하기
        Message getMessage = channelService.getMessage(channel1.getId(), "hello");
        System.out.println("getMessage = " + getMessage);

        // 채널의 모든 메시지 출력
        channelService.printChannel(channel1.getId());

        // 메시지 수정
        channelService.updateMessage(channel1.getId(), "nice", "replaceMessage");
        channelService.printChannel(channel1.getId());

        channelService.updateMessage(channel1.getId(), "replaceMessage", "nice");
        channelService.printChannel(channel1.getId());

        // 특정 메시지 삭제
        channelService.removeMessage(channel1.getId(), "nice");
        channelService.printChannel(channel1.getId());
    }

    public static User userEngine(DiscordService discordService) {
        //User 관련 기능 모음집
        // 사용자 등록
        // 사용자 이름과 비밀번호 받기
        User user1 = discordService.create();
        // 사용자의 이름만 받기
        User user2 = discordService.create("User2");
        User user3 = discordService.create("User3");


        //유저 불러오기
        user1 = discordService.get("User1");
        user2 = discordService.get("User2");
        user3 = discordService.get("User3");

        // 사용자 조회
        discordService.print();

        // 특정 데이터 조회
        User getUser = discordService.get("User3");
        System.out.println("get = \n" + getUser);

        // 수정
        discordService.update("User3", "replaceUser");
        discordService.print();

        discordService.update("replaceUser", "User3");
        discordService.print();

        //삭제
        discordService.remove(user3);
        discordService.print();

        return user1;
    }

    static boolean isAuto = true;

    public static void main(String[] args) {
        //초기 설정
        DiscordService discordService;
        UserService userService;
        ServerService serverService;
        ChannelService channelService;

        Scanner sc = new Scanner(System.in);
        System.out.println("JCF로 하시겠습니까? File로 하시겠습니까?");
        System.out.print("1: JCF // 2: File : ");
        int i = sc.nextInt();
        sc.nextLine();

        switch (i) {
            case 1:
                System.out.println("JCF 작동");
                discordService = new BasicDiscordService(JCFDiscordRepository.getInstance());
                userService = new BasicUserService(new JCFUserRepository());
                serverService = new BasicServerService(new JCFServerRepository());
                channelService = new BasicChannelService(new JCFChannelRepository());
                break;
            case 2:
                System.out.println("FILE 작동");
                discordService = new BasicDiscordService(FileDiscordRepository.getInstance());
                userService = new BasicUserService(new FileUserRepository());
                serverService = new BasicServerService(new FileServerRepository());
                channelService = new BasicChannelService(new FileChannelRepository());
                break;
            default:
                System.out.println("잘못된 값을 입력하셨습니다.");
                System.out.println("기본 셋팅인 File을 실행하겠습니다.");
                discordService = new BasicDiscordService(FileDiscordRepository.getInstance());
                userService = new BasicUserService(new FileUserRepository());
                serverService = new BasicServerService(new FileServerRepository());
                channelService = new BasicChannelService(new FileChannelRepository());
        }

        User user = userEngine(discordService);
        Server server = serverEngine(userService, user);
        Channel channel = channelEngine(serverService, server);
        messageEngine(channelService, channel);
    }
}
