package com.sprint.mission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import testmethod.ConsoleTestTemplate;

class ApplicationTest extends ConsoleTestTemplate {
    @Test
    void 채널_채팅입력_전체_시나리오_테스트() {
        run("3", "7팀", "2", "park@naver.com", "4", "Hello, 7team!!", "1", "Study", "5", "1", "6");
        assertThat(output()).contains(
                        "안녕하세요 코드잇2기 서버입니다.",
                        "—---------------------------------------------------------",
                        " 코드잇 2기  | general",
                        "—---------------------------------------------------------",
                        "# general |                                      | # 황지환",
                        "—---------------------------------------------------------",
                        "# 하고 싶은 기능 선택",
                        "- 다른 채널 생성 : 1번",
                        "- 현재 채널에 친구 추가 : 2번",
                        "- 현재 채널 이름변경 : 3번",
                        "- 현재 채널에 메세지 입력 : 4번",
                        "- 다른 채널 이동 : 5번",
                        "- 종료 : 6번",
                        "# 현재 채널을 어떤 이름으로 변경하시겠습니까? : ",

                        "—---------------------------------------------------------",
                        " 코드잇 2기  | 7팀",
                        "—---------------------------------------------------------",
                        "# 7팀      |                                      | # 황지환",
                        "—---------------------------------------------------------",
                        "# 친구 이메일 : ",

                        "—---------------------------------------------------------",
                        " 코드잇 2기  | 7팀",
                        "—---------------------------------------------------------",
                        "# 7팀      |                                      | # 황지환",
                        "          |                                      |   박지환",
                        "—---------------------------------------------------------",
                        "# 메세지 입력 : ",

                        "—---------------------------------------------------------",
                        " 코드잇 2기  | 7팀",
                        "—---------------------------------------------------------",
                        "# 7팀      | HWANGJIHWAN: Hello, 7team!!          | # 황지환",
                        "          |                                      |   박지환",
                        "—---------------------------------------------------------",
                        "# 생성할 채널의 이름을 설정해주세요 : ",

                        "—---------------------------------------------------------",
                        " 코드잇 2기  | Study",
                        "—---------------------------------------------------------",
                        "  7팀      |                                      | # 황지환",
                        "# Study   |                                      |       ",
                        "—---------------------------------------------------------",
                        "# 이동할 채널을 선택해주세요",
                        "- 7팀 : 1번",

                        "—---------------------------------------------------------",
                        " 코드잇 2기  | 7팀",
                        "—---------------------------------------------------------",
                        "# 7팀      | HWANGJIHWAN: Hello, 7team!!          | # 황지환",
                        "          |                                      |   박지환",
                        "—---------------------------------------------------------"
                );
    }

    @Override
    protected void runMain() {
        Application.main(new String[]{});
    }
}