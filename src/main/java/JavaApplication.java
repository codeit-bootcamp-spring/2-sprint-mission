import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.DiscordService;
import com.sprint.mission.discodeit.FrontEnd.FileDiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.FileDiscordService;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileServerService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class JavaApplication {
    public static Server serverEngine(UserService userService, User user) {
        //서버 생성
//        Server server1 = userService.createServer("Server1");
//        Server server2 = userService.createServer("Server2");
//        Server server3 = userService.createServer("Server3");

        //서버 등록
//        userService.addServer(user1.getId(), server1);
//        userService.addServer(user1.getId(), server2);
//        userService.addServer(user1.getId(), server3);

        //서버 불러오기
        Server server1 = userService.getServer(user.getId(), "Server1");
        Server server2 = userService.getServer(user.getId(), "Server2");
        Server server3 = userService.getServer(user.getId(), "Server3");

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

    public static Container channelEngine(ServerService serverService, Server server1) {
        // 채널 생성
//        Channel channel1 = serverService.createChannel("Channel1");
//        Channel channel2 = serverService.createChannel("Channel2");
//        Channel channel3 = serverService.createChannel("Channel3");

        //채널 등록
//        serverService.addChannel(server1.getId(),channel1);
//        serverService.addChannel(server1.getId(),channel2);
//        serverService.addChannel(server1.getId(),channel3);

        //채널 불러오기
        Container channel1 = serverService.getChannel(server1.getId(), "Channel1");
        Container channel2 = serverService.getChannel(server1.getId(), "Channel2");
        Container channel3 = serverService.getChannel(server1.getId(), "Channel3");

        // 모든 채널 조회
        serverService.printChannel(server1.getId());

        // 특정 채널 조회
        Container getChannel = serverService.getChannel(server1.getId(), "Channel1");
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

    public static void messageEngine(ChannelService channelService, Container channel1) {
        // 채널 내 메시지 생성 및 저장
//        channelService.write(channel1.getId(), "hello");
//        channelService.write(channel1.getId(), "world");
//        channelService.write(channel1.getId(), "nice");

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

    public static User userEngine(DiscordService discordService, DiscordRepository discordRepository) {
        //User 관련 기능 모음집
        // 사용자 등록
        // 사용자 이름과 비밀번호 받기
//        User user1 = discordService.create();
        // 사용자의 이름만 받기
//        User user2 = discordService.create("User2");

        //수동으로 생성 후 등록하기
//        User user3 = new User("User3", "123");
//        discordRepository.register(user3);

        //유저 불러오기
        User user1 = discordService.get("User1");
        User user2 = discordService.get("User2");
        User user3 = discordService.get("User3");

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

    public static void main(String[] args) {
        //초기 설정
        DiscordRepository discordRepository = FileDiscordRepository.getInstance();
        DiscordService discordService = FileDiscordService.getInstance();
        UserService userService = FileUserService.getInstance();
        ServerService serverService = FileServerService.getInstance();
        ChannelService channelService = FileChannelService.getInstance();
        //        MessageService messageService = JCFMessageService.getInstance();
        User user = userEngine(discordService, discordRepository);
        Server server = serverEngine(userService, user);
        Container channel = channelEngine(serverService, server);
        messageEngine(channelService, channel);
    }
}
