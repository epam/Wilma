//this is a level 17 code, unacceptable in java 15
import java.awt.*;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.IntStream;

public class InvalidClass {
    public InvalidClass() {
    }

    public IntStream getPseudoInts(String algorithm, int streamSize) {
        // returns an IntStream with size @streamSize of random numbers generated using the @algorithm
        // where the lower bound is 0 and the upper is 100 (exclusive)
        return RandomGeneratorFactory.of(algorithm)
                .create()
                .ints(streamSize, 0,100);
    }

    static String formatter(Object o) {
        String formatter = "unknown";
        if (o instanceof Integer i) {
            formatter = String.format("int %d", i);
        }
        return formatter;
    }

    public static void main(String[] var0) {
        System.out.println("=== RunnableTest ===");
        Runnable var1 = () -> {
            System.out.println("Hello world two!");
        };

        var1.run();
    }
}
