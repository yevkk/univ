public class RC5 {
    /**
     * Odd((e - 1) * 2^w),
     * where Odd is the nearest odd integer to the given input, e is the base of the natural logarithm
     */
    private static long P(int w) {
        return switch (w) {
            case 16 -> 0xB7E1;
            case 32 -> 0xB7E15163;
            default -> throw new IllegalArgumentException("supported w in {16, 32}");
        };
    }

    /**
     * Odd((&phi - 1) * 2^w),
     * where Odd is the nearest odd integer to the given input, where &phi is the golden ratio
     */
    private static long Q(int w) {
        return switch (w) {
            case 16 -> 0x9E37;
            case 32 -> 0x9E3779B9;
            default -> throw new IllegalArgumentException("supported w in {16, 32}");
        };
    }

    private static int lcs(int value, int count) {
        return (value << count | value >> (32 - count));
    }

    private static int rcs(int value, int count) {
        return (value >> count | value << (32 - count));
    }

    public static void main(String[] args) {

    }
}
