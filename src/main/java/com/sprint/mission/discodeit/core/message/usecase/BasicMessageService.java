package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.CreateBinaryContentUseCase;
import com.sprint.mission.discodeit.core.content.usecase.DeleteBinaryContentUseCase;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

  private final UserRepositoryPort userRepository;
  private final ChannelRepositoryPort channelRepository;
  private final MessageRepositoryPort messageRepository;

  private final CreateBinaryContentUseCase createBinaryContentUseCase;
  private final DeleteBinaryContentUseCase deleteBinaryContentUseCase;

  /**
   * <h2>메시지 생성 메서드</h2>
   * 유저와 채널을 조회한 뒤, 채널에 메시지를 넣는다. <br> Binary Content Service 중 create 사용하기위해 인터페이스 호출해서 사용함
   *
   * @param command               작성자 아이디, 작성될 곳의 채널 아이디, 작성한 내용
   * @param binaryContentCommands 첨부할 파일 이미지
   * @return 메시지 아이디, 생성 시각, 업데이트시각, 내용, 채널 아이디, 작성자, 첨부파일 목록
   */
  @Override
  @Transactional
  public MessageResult create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands) {
    //작성자가 존재하는 지 확인
    User user = userRepository.findById(command.authorId()).orElseThrow(
        () -> new NotFoundException(ErrorCode.USER_NOT_FOUND, command.authorId())
    );
    // 채널이 존재하는 지 확인
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    //첨부파일을 각각 조회해서 생성한 뒤 리스트로 변환
    List<BinaryContent> binaryContentIdList = binaryContentCommands.stream().map(
            createBinaryContentUseCase::create)
        .toList();

    //메시지 생성하기
    Message message = Message.create(user, channel, command.content(),
        binaryContentIdList);

    Message save = messageRepository.save(message);

    logger.info(
        "Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}, attachments {}",
        message.getId(), channel.getId(), user.getId(), message.getContent(),
        message.getAttachment());

    return MessageResult.create(save, user);
  }

//  @Override
//  public MessageResult find(UUID messageId, Pageable pageable) {
//    Slice<Message> messages = messageRepository.findById(messageId, pageable);
//    return MessageResult.create(message, message.getAuthor());
//  }

  /**
   * <h2>메시지 검색 메서드</h2>
   * 슬라이스를 이용해서 메시지를 조회한다. <br>
   *
   * @param channelId 찾을 채널의 아이디
   * @param cursor    생성 시각을 기준으로 조회를 한다.
   * @param pageable  사이즈 50, 생성 시각 별, 내림차순
   * @return [메시지 아이디, 생성 시각, 업데이트시각, 내용, 채널 아이디, 작성자, 첨부파일 목록]
   */
  @Override
  @Transactional(readOnly = true)
  public Slice<MessageResult> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<Message> messageSlice;

    //커서값이 없다면, cursor 없이 기존 페이지네이션 방식으로 조회
    if (cursor == null) {
      messageSlice = messageRepository.findAllByChannelId(channelId, pageable);
    } else {
      //커서가 존재하면 커서를 통해서 커서 페이지네이션 진행
      messageSlice = messageRepository.findAllByChannelId(channelId, cursor, pageable);
    }
    //메시지를 반환 dto로 변환하는 작업 진행
    return messageSlice.map(message -> MessageResult.create(message, message.getAuthor()));
  }

  /**
   * <h2>메시지 업데이트 로직</h2>
   * 메시지를 업데이트한다.
   *
   * @param command 새 텍스트 내용
   * @return 메시지 아이디, 생성 시각, 업데이트시각, 내용, 채널 아이디, 작성자, 첨부파일 목록
   */
  @Override
  @Transactional
  public MessageResult update(UpdateMessageCommand command) {
    Message message = messageRepository.findById(command.messageId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND, command.messageId()));

    message.update(command.newText());
    return MessageResult.create(message, message.getAuthor());
  }

  /**
   * <h2>메시지 삭제 메서드</h2>
   * 메세지를 삭제한다. <br> 삭제하면서 메시지 내부에 저장된 이미지 메타 데이터 역시 삭제한다. <br> 삭제할 때, binary Content 의 delete
   * usecase 인터페이스를 사용한다.
   *
   * @param messageId 삭제할 메시지 아이디
   */
  @Override
  @Transactional
  public void delete(UUID messageId) {
    //메시지 조회
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND, messageId));
    //메시지 내 첨부파일들 삭제
    message.getAttachment()
        .forEach(binaryContent -> deleteBinaryContentUseCase.delete(binaryContent.getId())
        );
    //메시지 삭제
    messageRepository.delete(messageId);
  }
}
