import com.sprint.mission.discodeit.FrontEnd.DiscordService;
import com.sprint.mission.discodeit.FrontEnd.JCFDiscordService;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFServerService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {


    public static void main(String[] args) {
        //초기 설정
        DiscordService discordService = JCFDiscordService.getInstance();
        UserService userService = JCFUserService.getInstance();
        ServerService serverService = JCFServerService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();

        //User 관련 기능 모음집
        // 사용자 등록
        // 사용자 이름과 비밀번호 받기
        User user = discordService.create();

        // 사용자의 이름만 받기
        User get = discordService.create("get");

        //수동으로 생성 후 등록하기
        User remove = new User("remove", "123");
        User update = new User("update", "123");

//        discordService.register(remove);
//        discordService.register(update);

        // 사용자 조회
        discordService.print();

        // 특정 데이터 조회
        User getUser = discordService.get("get");
        System.out.println("getUser.getName() = " + getUser.getName());

        // 수정
        discordService.update(update);

        // 사용자 조회
        discordService.print();

        //서버 생성
        Server s = userService.createServer("server");
        Server getServer = userService.createServer("get");
        Server removeServer = userService.createServer("remove");
        Server updateServer = userService.createServer("update");


        //서버 등록
        userService.addServer(user.getId(), s);
        userService.addServer(user.getId(), getServer);
        userService.addServer(user.getId(), updateServer);
        userService.addServer(user.getId(), removeServer);

        //유저가 가지고 있는 서버 조회
        userService.printServer(user.getId());

        System.out.println("\n서버 주입하지 않는 get유저는 서버가 없어야 정상");
        userService.printServer(get.getId());

        //유저 서버 주입 후 조회
        userService.addServer(get.getId(),removeServer);
        userService.addServer(get.getId(),updateServer);
        userService.printServer(get.getId());

        // 특정 데이터 조회
        userService.getServer(user.getId(), getServer.getName());

        // 수정
        userService.updateServer(user.getId(), updateServer.getName(),"changeChannel");

        //수정 후 조회
        userService.printServer(user.getId());
        userService.printServer(get.getId());

        //서버 삭제
        userService.removeServer(user.getId(), removeServer.getName());

        // 서버 삭제 후 서버 조회
        userService.printServer(user.getId());
        userService.printServer(get.getId());

        // 채널 생성
        Channel c = serverService.createChannel("channel");
        Channel getChannel = serverService.createChannel("get");
        Channel updateChannel = serverService.createChannel("update");
        Channel removeChannel = serverService.createChannel("remove");

        //채널 등록
        serverService.addChannel(s.getId(),c);
        serverService.addChannel(s.getId(),getChannel);
        serverService.addChannel(s.getId(),removeChannel);
        serverService.addChannel(s.getId(),updateChannel);

        // 모든 채널 조회
        serverService.printChannel(s.getId());

        // 특정 채널 조회
        serverService.getChannel(s.getId(), getChannel.getName());

        // 채널명 수정
        serverService.updateChannel(s.getId(), updateChannel.getName(),"changeChannel");

        // 수정 후 조회
        serverService.printChannel(s.getId());

        // 채널 삭제
        serverService.removeChannel(s.getId(), removeChannel.getName());

        // 채널 삭제 후 모든 채널 조회
        serverService.printChannel(s.getId());


        // 채널 내 메시지 생성
        Message m = channelService.write(c.getId(), "1");
        Message getMessage = channelService.write(c.getId(), "get");
        Message removeMessage = channelService.write(c.getId(), "remove");
        Message updateMessage = channelService.write(c.getId(), "update");

        //특정 메시지 조회
        System.out.println("getMessage.getName() = " + channelService.getMessage(c.getId(), getMessage.getStr()).getStr());

        // 채널의 모든 메시지 출력
        channelService.printChannel(c.getId());

        // 특정 메시지 삭제
        channelService.removeMessage(c.getId(), removeMessage.getStr());

        // 메시지 삭제 후 채널의 모든 메시지 출력
        channelService.printChannel(c.getId());

        // 메시지 수정
        channelService.updateMessage(c.getId(), updateMessage.getStr(), "replaceChannelMessage");

        // 메시지 수정 후 채널 메시지 출력
        channelService.printChannel(c.getId());

        // 사용자끼리 메시지 전송
        User target = discordService.create();
        messageService.send(user.getId(), target.getId(), "hello");
        messageService.send(user.getId(), target.getId(), "1");

        // 사용자 메시지 조회
        messageService.read(user.getId());
        messageService.read(target.getId());

        // 사용자 메시지 수정
        messageService.update(user.getId(), target.getId(), "1", "world");

        // 사용자 메시지 조회
        messageService.read(user.getId());
        messageService.read(target.getId());

        // 사용자 메시지 삭제
        messageService.remove(user.getId(), target.getId(), "world");

        // 사용자 메시지 조회
        messageService.read(user.getId());
        messageService.read(target.getId());

    }
}
