import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.Exception.EmptyException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.Repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceTest {
    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository();
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();
        UserStatusRepository userStatusRepository = new FileUserStatusRepository();

        BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        UserService userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);


        System.out.println("register----------------------------------------");
        byte[] testBytes = {0, 0, 1};
//        BinaryContentDTO binaryContentDTO1 = BinaryContentDTO.create("test1", "test", testBytes);
//        BinaryContentDTO binaryContentDTO2 = BinaryContentDTO.create("test2", "test", testBytes);
//
//        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123");
//        UserCRUDDTO userDTO2 = UserCRUDDTO.create("test2", "test2", "123");
//        UserCRUDDTO userDTO3 = UserCRUDDTO.create("test1", "test1", "123");
//
//        User user = userService.register(userDTO1, Optional.of(binaryContentDTO1));
//        User user1 = userService.register(userDTO2, Optional.of(binaryContentDTO2));
//        User user2 = userService.register(userDTO3, Optional.empty());

//        UUID test1 = user.getId();
//        UUID test2 = user1.getId();

        UUID test1 = UUID.fromString("fe4b4038-1c06-4899-bfc5-9356f24d3f2f");
        UUID test2 = UUID.fromString("b0db0189-4b14-4832-99b0-c103e65f248b");

        List<UserFindDTO> userFindDTOS1 = userService.findAll();
        for (UserFindDTO userFindDTO : userFindDTOS1) {
            System.out.println(userFindDTO);
        }
        System.out.println("status find----------------------------------------");
        List<UserStatus> statusList = userStatusRepository.findAll();
        for (UserStatus userStatus : statusList) {
            System.out.println(userStatus);
        }
        System.out.println("BinaryContent find----------------------------------------");
        try {
            List<BinaryContent> contentList = binaryContentRepository.findAllByIdIn();
            for (BinaryContent content : contentList) {
                System.out.println(content);
            }
        } catch (EmptyException e) {
            System.out.println("바이너리 리스트가 비어있습니다.");
        }


        System.out.println("find----------------------------------------");
        UserFindDTO findTest1 = userService.find(test1);
        System.out.println(findTest1);

        System.out.println("delete----------------------------------------");
        UserCRUDDTO delete = UserCRUDDTO.delete(test2);
        userService.delete(delete);


        List<UserFindDTO> userFindDTOS2 = userService.findAll();
        for (UserFindDTO userFindDTO : userFindDTOS2) {
            System.out.println(userFindDTO);
        }

        System.out.println("update----------------------------------------");
        byte[] bytes = {0, 1, 0};
        BinaryContentDTO binaryContentDTO3 = BinaryContentDTO.create("test3", null, bytes);
        BinaryContent content3 = binaryContentService.create(binaryContentDTO3);

        UserCRUDDTO update = UserCRUDDTO.update(null, content3.getBinaryContentId(), null, null);
        userService.update(test1, update);

        List<UserFindDTO> userFindDTOS3 = userService.findAll();
        for (UserFindDTO userFindDTO : userFindDTOS3) {
            System.out.println(userFindDTO);
        }
        System.out.println("status find----------------------------------------");
        List<UserStatus> statusList2 = userStatusRepository.findAll();
        for (UserStatus userStatus : statusList2) {
            System.out.println(userStatus);
        }

        System.out.println("BinaryContent find----------------------------------------");
        List<BinaryContent> contentList2 = binaryContentRepository.findAllByIdIn();
        for (BinaryContent content : contentList2) {
            System.out.println(content);
        }
    }
}
