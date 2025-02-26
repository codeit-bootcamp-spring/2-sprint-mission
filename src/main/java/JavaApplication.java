import static com.sprint.mission.discodeit.MainEntry.*;

public class JavaApplication {


    public static void main(String[] args) {
        //User 관련 기능 모음집
        // 사용자 등록
        createUser();

        // 모든 사용자 조회
        printUser();

        // 사용자 전환
        replaeHead();

        // 특정 데이터 조회
        search();

        // 수정
        update();


        //서버 등록
        createServer();
        createServer();
        createServer();

        //유저가 가지고 있는 서버 조회
        printServer();

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

        // 채널 삭제
        removeChannel();
        removeChannel();

        // 채널 삭제 후 모든 채널 조회
        printChannel();

        // 특정 채팅방 조회


        // 사용자가 속한 채팅방 조회


        // 채널명 수정
        // 수정 시 채널 전부 수정되는 문제 발견


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
