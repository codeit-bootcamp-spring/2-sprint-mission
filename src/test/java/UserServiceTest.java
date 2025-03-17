import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class UserServiceTest {
    public static void main(String[] args) {
        UserRepository userRepository = new JCFUserRepository();
        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        UserService userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);

        BinaryContentDTO binaryContentDTO1 = BinaryContentDTO.create("test1", null, null);
        BinaryContentDTO binaryContentDTO2 = BinaryContentDTO.create("test2", null, null);
        BinaryContent content1 = binaryContentService.create(binaryContentDTO1);
        BinaryContent content2 = binaryContentService.create(binaryContentDTO2);

        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123",content1);
        UserCRUDDTO userDTO2 = UserCRUDDTO.create("test2", "test2", "123",content2);
        UserCRUDDTO userDTO3 = UserCRUDDTO.create("test1", "test1", "123",null);

        UUID test1 = userService.register(userDTO1);
        UUID test2 = userService.register(userDTO2);
        UUID test3 = userService.register(userDTO3);
        System.out.println("register----------------------------------------");

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
        List<BinaryContent> contentList = binaryContentRepository.findAllByIdIn();
        for (BinaryContent content : contentList) {
            System.out.println(content);
        }

        System.out.println("find----------------------------------------");
        UserFindDTO findTest1 = userService.find(test1.toString());
        System.out.println(findTest1);

        System.out.println("delete----------------------------------------");
        UserCRUDDTO delete = UserCRUDDTO.delete(test2);
        userService.delete(delete);


        List<UserFindDTO> userFindDTOS2 = userService.findAll();
        for (UserFindDTO userFindDTO : userFindDTOS2) {
            System.out.println(userFindDTO);
        }

        System.out.println("update----------------------------------------");
        BinaryContentDTO binaryContentDTO3 = BinaryContentDTO.create("test3", null, null);
        BinaryContent content3 = binaryContentService.create(binaryContentDTO3);

        UserCRUDDTO update = UserCRUDDTO.update(null, content3.getBinaryContentId(), null, null);
        userService.update(test1.toString(), update);

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
