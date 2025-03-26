package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentDeleteDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentFindDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

//
//
//		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
//
//		List<BinaryContent> print = binaryContentService.getAllUser();
//		System.out.println(print);
//
//
//		System.out.println();
//		System.out.println();
//		System.out.println();
//
//
//		String userUuid1 = "31802009-fe37-481c-8d0e-a7fcac00dcd3";
//		UUID userUuid2 = UUID.fromString(userUuid1);
//		BinaryContentFindDto find = new BinaryContentFindDto(userUuid2);
//		BinaryContent print1 = binaryContentService.getUser(find);
//		System.out.println(print1);
//
//		BinaryContentDeleteDto delete = new BinaryContentDeleteDto(userUuid2);
//		binaryContentService.delete(delete);
	}

}
