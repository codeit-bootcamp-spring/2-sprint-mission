package testmethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/* 원본은 camp.nextstep.edu 라이브러리에 있습니다.
   이해를 위해 주석을 작성했습니다. 추후 보강하겠습니다
* */
public abstract class ConsoleTestTemplate {
    private PrintStream standardOut;
    private OutputStream captor;

    // 처음에 null을 기록해놓고, 출력값을 캡쳐할 준비를 합니다
    @BeforeEach
    protected final void init() {
        standardOut = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    // 출력 콘솔을 처음 상태로 되돌리고
    // 캡처된 결과를 출력합니다
    @AfterEach
    protected final void printOutput() {
        System.setOut(standardOut);
        System.out.println(output());
    }

    // 바이트로되어있던걸 문자열로 변환합니다.
    protected final String output() {
        return captor.toString().trim();
    }

    // 1. 매겨변수를 콘솔에 입력값으로 2. 자식에서 구현된 main 함수 실행 시키기
    protected final void run(final String... args) {
        command(args);
        runMain();
    }

    // args에 아무것도 입력이 없을때만 예외를 처리합니다
    protected final void runException(final String... args) {
        try {
            run(args);
        } catch (final NoSuchElementException ignore) {
        }
    }

    // 매개변수를 콘솔입력 값으로 받아오기 위해 사용합니다
    private void command(final String... args) {
        final byte[] buf = String.join("\n", args).getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

    protected abstract void runMain();
}