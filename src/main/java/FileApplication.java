import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;

public class FileApplication {
    public static void main(String[] args) {

        UserService userService = FileUserService.getInstance();
        // 1. 유저
        // 1.1 유저 생성
        User user1 = new User("yyjjmm2003@naver.com", "1234", "고기");
        User user2 = new User("sjrnfldbcjs@naver.com", "1234", "무민");
        // User user3 = new User("yyjjmm2003@naver.com", "1234", "고기"); - 중복 유저이므로 예외 발생
        userService.createUser(user1);
        userService.createUser(user2);
        // userService.createUser(user3);


        // 1.2 유저 조회
        User findUser = userService.getUser("yyjjmm2003@naver.com");
        List<User> findUsers = userService.getAllUsers();
        System.out.println(findUser);
        System.out.println(findUsers);


        // 1.3 유저 수정
        User updateUser1 = new User("yyjjmm2003@naver.com", "수정1234", "수정고기");
        userService.updateUser(updateUser1); // 파일까지 잘 변경됨
        // Role 추가
        userService.addRole("Customer", updateUser1.getUsername());
        User findUser2 = userService.getUser("yyjjmm2003@naver.com");
        System.out.println(findUser2);


        // 1.4 유저 삭제
        userService.deleteUser("yyjjmm2003@naver.com");
        System.out.println(userService.getAllUsers());






    }
}
