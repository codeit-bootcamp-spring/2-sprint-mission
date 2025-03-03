package testmethod;

import com.sprint.mission.discodeit.view.Console;
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

    @BeforeEach
    protected final void init() {
        standardOut = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @AfterEach
    protected final void printOutput() {
        System.setOut(standardOut);
        System.out.println(output());
    }

    protected final String output() {
        return captor.toString().trim();
    }

    protected final void run(final String... args) {
        try {
            command(args);
            runMain();
        } finally {
            Console.close();
        }
    }

    protected final void runException(final String... args) {
        try {
            run(args);
        } catch (final NoSuchElementException ignore) {
        }
    }

    private void command(final String... args) {
        final byte[] buf = String.join("\n", args).getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

    protected abstract void runMain();
}