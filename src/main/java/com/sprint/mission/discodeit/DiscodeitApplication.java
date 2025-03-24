package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.dto.*;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService  = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		UserStatusService userStatusService = context.getBean(UserStatusService.class);
		ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

		Test test = new Test(userService, channelService, messageService, userStatusService, readStatusService, binaryContentService);

		//1. User 생성 및 조회
		test.createUser("hanna@email.net", "12345*", "hanna", UserStatusType.OFFLINE, UserRole.ADMIN);
		test.createUser("dog@email.net", "54321*", "dog", UserStatusType.OFFLINE, UserRole.USER);

		UserResponseDto userResponseDto = userService.findByEmail("hanna@email.net");
		UUID userId = userResponseDto.id();

		//2. Channel 생성 및 조회
		test.createChannel(ChannelType.PUBLIC, "공지사항", "학습공지", userId, UserRole.ADMIN);
		List<ChannelResponseDto> channels = channelService.findAllByUserId(userId);
		ChannelResponseDto channel = channels.get(0);
		UUID channelId = channel.id();

		//3. ReadStatus 생성: 채널의 읽기 상태
		test.createReadStatus(userId, channelId, Instant.now().minusSeconds(120));

		//4. UserStatus 수정: 사용자 접속 상태 update
		//login 추가 필요
		test.updateUserStatus(userId, Instant.now().minusSeconds(300));

		//5. User 수정
		test.updateUser(userId, "newpassword*", "new_hanna", UserStatusType.ONLINE, UserRole.ADMIN);

		//6. Channel 수정
		test.updateChannel(channelId, "공지사항_v2", "학습공지_v2", ChannelType.PUBLIC, userId, channel.writePermission());

		//7. BinaryContent 생성: 파일 첨부
        Path filePath = Paths.get("fileInput/textFile.txt");
        byte[] fileData = FileUtil.loadFileToBytes(filePath);
		String fileName = filePath.getFileName().toString();
		long fileSize = 0L;
		String fileType = "";
		try {
			fileSize = Files.size(filePath);
			fileType = Files.probeContentType(filePath);
		} catch (IOException e) {
			throw new RuntimeException("파일 크기 또는 타입 가져오기 실패: " + filePath, e);
		}
		test.createBinaryContent(fileData, filePath.toString(), fileName, fileType, fileSize);

		//5. Message 생성
		test.createMessage(userId, channelId, "공지글입니다. 확인해주세요.");

		//6. Message 수정
		List<MessageResponseDto> messages = messageService.findAllByChannelId(channelId);
		MessageResponseDto message = messages.get(0);
		UUID messageId = message.id();
		test.updateMessasge(messageId, "공지가 취소되었습니다.", userId, channelId);

		//7. Message 삭제
		test.deleteMessage(messageId, userId, channelId);

		//8. 예외 테스트
		UserResponseDto userResponseDto2 = userService.findByEmail("dog@email.net");
		UUID notAdminUserId = userResponseDto2.id();
		test.exceptionTest(channelId, notAdminUserId);

		//9. Channel 삭제
		test.deleteChannel(channelId, userId);

		//8. User 삭제
		test.deleteUser(userId);
	}

	static class Test{
		private final UserService userService;
		private final ChannelService channelService;
		private final MessageService messageService;
		private final UserStatusService userStatusService;
		private final ReadStatusService readStatusService;
		private final BinaryContentService binaryContentService;

		public Test(UserService userService,
					ChannelService channelService,
					MessageService messageService,
					UserStatusService userStatusService,
					ReadStatusService readStatusService,
					BinaryContentService binaryContentService) {
			this.userService = userService;
			this.channelService = channelService;
			this.messageService = messageService;
			this.userStatusService = userStatusService;
			this.readStatusService = readStatusService;
			this.binaryContentService = binaryContentService;
		}

		private void createUser(String email, String password, String nickname, UserStatusType status, UserRole role) {
			System.out.println("===================== User create Test =========================");
			UserCreateDto createDto = UserCreateDto.withoutProfile(email, password, nickname, status, role);
			userService.createUser(createDto);
			System.out.println("등록된 유저 조회: " + userService.findAll());
			System.out.println("=========================================================");
		}

		private void updateUser(UUID userId, String password, String nickname, UserStatusType status, UserRole userRole) {
			System.out.println("===================== User update Test =========================");
			UserUpdateDto updateDto = UserUpdateDto.withoutProfile(userId, password, nickname, status, userRole);
			userService.updateUser(updateDto);
			System.out.println("수정된 유저 조회: " + userService.findById(userId));
			System.out.println("=========================================================");
		}

		private void deleteUser(UUID userId) {
			System.out.println("===================== User delete Test ========================");
			userService.deleteUser(userId);
			System.out.println("유저 삭제 후 유저 목록 조회: " + userService.findAll());
			System.out.println("=========================================================");
		}

		private void createChannel(ChannelType type, String category, String name, UUID userId, UserRole userRole) {
			System.out.println("===================== Channel create Test =========================");
			ChannelCreateDto createDto = new ChannelCreateDto(type, category, name, userId, null, userRole);
			channelService.createPublicChannel(createDto);
			System.out.println("등록된 채널 조회: " + channelService.findAllByUserId(userId));
			System.out.println("=========================================================");
		}

		private void updateChannel(UUID channelId, String name, String category, ChannelType type, UUID userId, UserRole writePermission) {
			System.out.println("===================== Channel update Test =========================");
			ChannelUpdateDto updateDto = new ChannelUpdateDto(channelId, type, category, name, userId, writePermission);
			channelService.updateChannel(updateDto);
			System.out.println("수정된 채널 조회: " + channelService.findChannelById(channelId));
			System.out.println("=========================================================");
		}

		private void deleteChannel(UUID channelId, UUID userId) {
			System.out.println("===================== Channel delete Test ========================");
			channelService.deleteChannel(channelId, userId);
			System.out.println("=========================================================");
		}

		private void createMessage(UUID userId, UUID channelId, String content) {
			System.out.println("===================== Message create Test =========================");
			MessageCreateDto createDto = MessageCreateDto.withoutFile(userId, channelId, content);
			messageService.createMessage(createDto);
			System.out.println("등록된 메시지 조회: " + messageService.findAllByChannelId(channelId));
			System.out.println("=========================================================");
		}

		private void updateMessasge(UUID messageId, String content, UUID userId, UUID channelId) {
			System.out.println("===================== Message update Test =========================");
			MessageUpdateDto updateDto = MessageUpdateDto.withoutFile(messageId, userId, channelId, content);
			messageService.updateMessage(updateDto);
			System.out.println("수정된 메시지 조회: " + messageService.findById(messageId));
			System.out.println("=========================================================");
		}

		private void deleteMessage(UUID messageId, UUID userId, UUID channelId) {
			System.out.println("===================== Message delete Test ========================");
			messageService.deleteMessage(messageId, userId, channelId);
			System.out.println("=========================================================");
		}

		private void createReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
			System.out.println("===================== ReadStatus create Test =========================");
			ReadStatusCreateDto createDto = new ReadStatusCreateDto(userId, channelId, lastReadAt);
			readStatusService.createReadStatus(createDto);
			System.out.println("유저의 읽기 상태 조회: " + readStatusService.findAllByUserId(userId));
			System.out.println("=========================================================");
		}

		private void updateUserStatus(UUID userId, Instant lastReadAt) {
			System.out.println("===================== UserStatus update Test =========================");
			UUID id = userStatusService.findByUserId(userId).id();
			UserStatusUpdateDto updateDto = new UserStatusUpdateDto(id, userId, lastReadAt);
			userStatusService.updateUserStatus(updateDto);
			System.out.println("변경된 접속 상태 조회: " + userStatusService.findAll());
			System.out.println("유저의 접속 상태 조회: " + userService.findById(userId).isOnline());
			System.out.println("=========================================================");
		}

		private void createBinaryContent(byte[] fileData, String filePath, String fileName, String fileType, long fileSize) {
			System.out.println("===================== BinaryContent create Test =========================");
			BinaryContentCreateDto createDto = new BinaryContentCreateDto(fileData, filePath, fileName, fileType, fileSize);
			System.out.println("fileName===>"+fileName);
			binaryContentService.createBinaryContent(createDto);
			System.out.println("등록된 파일 조회: " + binaryContentService.findAll());
			System.out.println("=========================================================");
		}

		private void exceptionTest(UUID channelId, UUID userId){
			System.out.println("===================== Exception Test ========================");
			try {
				channelService.addMembers(channelId, Set.of(userId), userId);
			} catch (RuntimeException e) {
				System.out.println("멤버 추가 시 예외 발생: " + e.getMessage());
			}

			try {
				MessageCreateDto messageForTest = MessageCreateDto.withoutFile(userId, channelId, "일반 유저가 메시지를 작성합니다.");
				messageService.createMessage(messageForTest);
			} catch (RuntimeException e) {
				System.out.println("메시지 작성 시 예외 발생: " + e.getMessage());
			}

			System.out.println("=========================================================");
		}


	}
}
