public class JavaApplication {
//    public static Server serverEngine(UserService userService, User user) {
//        //서버 생성
//        Server server1 = userService.createServer("server1");
//        Server server2 = userService.createServer("server2");
//        Server server3 = userService.createServer("server3");
//
//
//        //서버 등록
//        if (isJCF || !saveServerData) {
//            userService.addServer(user.getId(), server1);
//            userService.addServer(user.getId(), server2);
//            userService.addServer(user.getId(), server3);
//        }
//
//        //서버 불러오기
//        server1 = userService.getServer(user.getId(), "server1");
//        server2 = userService.getServer(user.getId(), "server2");
//        server3 = userService.getServer(user.getId(), "server3");
//
//        //유저가 가지고 있는 서버 조회
//        userService.printServer(user.getId());
//
//
//        // 특정 데이터 조회
//        Server getServer = userService.getServer(user.getId(), "server1");
//        System.out.println("getServer = \n" + getServer);
//
//        // 서버 수정
//        userService.updateServer(user.getId(), "server3", "replaceServer");
//        userService.printServer(user.getId());
//
//        //서버명 복원
//        userService.updateServer(user.getId(), "replaceServer", "server3");
//        userService.printServer(user.getId());
//
//        //서버 삭제
//        Server remove = userService.createServer("remove");
//        userService.addServer(user.getId(), remove);
//        userService.printServer(user.getId());
//
//        userService.removeServer(user.getId(), "remove");
//        userService.printServer(user.getId());
//
//        return server1;
//
//    }
//
//    public static Channel channelEngine(ServerService serverService, Server server1) {
//        // 채널 생성
//        Channel channel1 = serverService.createChannel("channel1");
//        Channel channel2 = serverService.createChannel("channel2");
//        Channel channel3 = serverService.createChannel("channel3");
//
//
//        //채널 등록
//        if (isJCF || !saveContainerData) {
//            serverService.addChannel(server1.getServerId(), channel1);
//            serverService.addChannel(server1.getServerId(), channel2);
//            serverService.addChannel(server1.getServerId(), channel3);
//        }
//        //채널 불러오기
//        channel1 = serverService.getChannel(server1.getServerId(), "channel1");
//        channel2 = serverService.getChannel(server1.getServerId(), "channel2");
//        channel3 = serverService.getChannel(server1.getServerId(), "channel3");
//
//        // 모든 채널 조회
//        serverService.printChannel(server1.getServerId());
//
//        // 특정 채널 조회
//        Channel getChannel = serverService.getChannel(server1.getServerId(), "channel1");
//        System.out.println("getChannel = \n" + getChannel);
//
//        // 채널명 수정
//        serverService.updateChannel(server1.getServerId(), "channel3", "replaceChannel");
//        serverService.printChannel(server1.getServerId());
//
//        serverService.updateChannel(server1.getServerId(), "replaceChannel", "channel3");
//        serverService.printChannel(server1.getServerId());
//
//        // 채널 삭제
//        Channel remove = serverService.createChannel("remove");
//        serverService.addChannel(server1.getServerId(), remove);
//        serverService.printChannel(server1.getServerId());
//
//        serverService.removeChannel(server1.getServerId(), "remove");
//        serverService.printChannel(server1.getServerId());
//
//        return channel1;
//    }
//
//    public static void messageEngine(ChannelService channelService, Channel channel1) {
//        // 채널 내 메시지 생성 및 저장
//        if (isJCF || ! saveMessageData) {
//            channelService.write(channel1.getId(), "hello");
//            channelService.write(channel1.getId(), "world");
//            channelService.write(channel1.getId(), "nice");
//        }
//
//        //저장된 특정 메시지 조회하기
//        Message getMessage = channelService.getMessage(channel1.getId(), "hello");
//        System.out.println("getMessage = " + getMessage);
//
//        // 채널의 모든 메시지 출력
//        channelService.printChannel(channel1.getId());
//
//        // 메시지 수정
//        channelService.updateMessage(channel1.getId(), "nice", "replaceMessage");
//        channelService.printChannel(channel1.getId());
//
//        channelService.updateMessage(channel1.getId(), "replaceMessage", "nice");
//        channelService.printChannel(channel1.getId());
//
//        // 특정 메시지 삭제
//        channelService.write(channel1.getId(), "remove");
//        channelService.printChannel(channel1.getId());
//
//        channelService.removeMessage(channel1.getId(), "remove");
//        channelService.printChannel(channel1.getId());
//    }
//
//    public static User userEngine(DiscordService discordService) {
//        User user1;
//        User user2;
//        User user3;
//
//        // 사용자 등록
//        if (isJCF || !saveUserData) {
//            // 사용자 이름과 비밀번호 받기
//            user1 = discordService.create();
//            // 사용자의 이름만 받기
//            user2 = discordService.create("user2");
//            user3 = discordService.create("user3");
//        }
//
//        //유저 불러오기
//        user1 = discordService.get("user1");
//        user2 = discordService.get("user2");
//        user3 = discordService.get("user3");
//
//        // 사용자 조회
//        discordService.print();
//
//        // 특정 데이터 조회
//        User getUser = discordService.get("user3");
//        System.out.println("get = \n" + getUser);
//
//        // 수정
//        discordService.update("user3", "replaceUser");
//        discordService.print();
//
//        discordService.update("replaceUser", "user3");
//        discordService.print();
//
//        //삭제
//        User remove = discordService.create("remove");
//        discordService.print();
//
//        discordService.remove(remove);
//        discordService.print();
//
//        return user1;
//    }
//
//    //저장된 값 존재여부 확인
//    static boolean saveUserData;
//    static boolean saveMessageData;
//    static boolean saveContainerData;
//    static boolean saveServerData;
//
//    static boolean isJCF = false;
//
//    public static void main(String[] args) {
//        saveUserData = Files.exists(Paths.get(System.getProperty("user.dir"), "data", "UserList.ser"));
//        saveServerData = Files.exists(Paths.get(System.getProperty("user.dir"), "data", "serverList.ser"));
//        saveContainerData = Files.exists(Paths.get(System.getProperty("user.dir"), "data", "ContainerList.ser"));
//        saveMessageData = Files.exists(Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser"));
//        //초기 설정
//        DiscordService discordService;
//        UserService userService;
//        ServerService serverService;
//        ChannelService channelService;
//
//        Scanner sc = new Scanner(System.in);
//        System.out.println("JCF로 하시겠습니까? File로 하시겠습니까?");
//        System.out.print("1: JCF // 2: File : ");
//        int i = sc.nextInt();
//        sc.nextLine();
//        switch (i) {
//            case 1:
//                System.out.println("JCF 작동");
//                isJCF = true;
//                discordService = new BasicDiscordService(JCFDiscordRepository.getInstance());
//                userService = new BasicUserService(new JCFUserRepository());
//                serverService = new BasicServerService(new JCFServerRepository());
//                channelService = new BasicChannelService(new JCFChannelRepository());
//                break;
//            case 2:
//                System.out.println("FILE 작동");
//                discordService = new BasicDiscordService(FileDiscordRepository.getInstance());
//                userService = new BasicUserService(new FileUserRepository());
//                serverService = new BasicServerService(new FileServerRepository());
//                channelService = new BasicChannelService(new FileChannelRepository());
//                break;
//            default:
//                System.out.println("잘못된 값을 입력하셨습니다.");
//                System.out.println("기본 셋팅인 File을 실행하겠습니다.");
//                discordService = new BasicDiscordService(FileDiscordRepository.getInstance());
//                userService = new BasicUserService(new FileUserRepository());
//                serverService = new BasicServerService(new FileServerRepository());
//                channelService = new BasicChannelService(new FileChannelRepository());
//        }
//        User user = userEngine(discordService);
//        Server server = serverEngine(userService, user);
//        Channel channel = channelEngine(serverService, server);
//        messageEngine(channelService, channel);
//    }
}
