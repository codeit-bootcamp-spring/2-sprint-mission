package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserServiceimplement implements UserService {
    UserRepository userRepository;

    public JCFUserServiceimplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createdUser(User user) {
        if (userRepository.containsUser(user.getuserId())) {
            System.out.println(user.getuserId() + "중복된 아이디입니다.");
            System.out.println("다시입력해 주십시오");
          //  return false;
            return;
        }
            boolean value = userRepository.registerUserId(user);
            if (value) {
                System.out.println(user.getuserId() + " 생성완료");
                userRepository.registerUserId(user);
            }
    }


    @Override
    public void readUser(String userId) {
        if (!userRepository.containsUser(userId)) {
            System.out.println("유저 정보 없음: " + userId);
            return;
        }
        System.out.println(userRepository.readuser(userId)); //tostring
    }

    @Override
    public void readAllUsers() {
        List<String> alluserList = userRepository.allReadUsers();
        alluserList.forEach(user -> System.out.println("유저 : "+user));
    }

    @Override
    public boolean updateUser(String userId, String updateUserId) {
        if (!userRepository.containsUser(updateUserId)) { //유저존재 and 바꿀 이름이 중복x
                System.out.println(userRepository.readuser(userId));
                return true;
            }
        System.out.println("중복입니다.");
        return false;
    }


    @Override
    public void deleteUser(String userId) {
        if (userRepository.containsUser(userId)) {
            boolean value = userRepository.removeUser(userId);
            if (!value) {
                System.out.println("성공하였습니다");
            }

        }
    }
}
