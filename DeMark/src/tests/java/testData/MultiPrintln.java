package testData;

public class MultiPrintln {
    public static int foo() {
        <caret>int x = 5;
        int y = 5;
        int z = x + y;
        System.out.println(z);
        return z;
    }

    public static String bar(int y) {
        System.out.println(22);
        boolean isEven = y % 2 == 0;
        return "Return " + y;
    }

    public static void baz() {
        System.out.print("Hello ");
        System.out.println("world");
    }
}
