import static com.sprint.mission.discodeit.MainEntry.*;

public class JavaApplication {


    public static void main(String[] args) {
        //User 관련 기능 모음집
        // 사용자 등록
        createUser();

        // 모든 사용자 조회
        printUser();

        // 현재 바라보고 있는 사용자 조회
        printUserHead();

        // 사용자 전환
        replaeUserHead();

        // 특정 데이터 조회
        searchUser();

        // 수정
        updateUser();

        //서버 등록
        createServer();
        createServer();
        createServer();

        //유저가 가지고 있는 서버 조회
        printServer();

        // 현재 바라보고 있는 사용자 조회
        printServerHead();

        // 사용자 전환
        replaeServerHead();

        // 특정 데이터 조회
        searchServer();

        // 수정
        updateServer();

        //서버 삭제
        removeServer();
        removeServer();

        // 서버 삭제 후 모든 서버 조회
        printServer();

        // 채널 등록
        createChannel();
        createChannel();
        createChannel();

        // 모든 채널 조회
        printChannel();

        // 현재 바라보고 있는 채널 조회
        printChannelHead();

        // 사용자 전환
        replaeChannelHead();

        // 특정 채널 조회
        searchChannel();

        // 채널명 수정
        updateChannel();

        // 채널 삭제
        removeChannel();
        removeChannel();

        // 채널 삭제 후 모든 채널 조회
        printChannel();

        // 메시지 생성
        writeMessage();
        writeMessage();
        writeMessage();

        // 채널의 모든 메시지 출력
        printAllMessage();

        // 특정 메시지 삭제
        removeMessage();

        // 메시지 삭제 후 채널의 모든 메시지 출력
        printAllMessage();


    }
}
