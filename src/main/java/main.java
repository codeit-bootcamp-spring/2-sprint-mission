import java.util.stream.IntStream;

public class main {
    public static void main(String[] args) {
        long count = IntStream.of(1, 3, 5, 7, 9).count();
        System.out.println(count);
    }
}
