public class RC5 {
    /**
     * Odd((e - 1) * 2^w),
     * where Odd is the nearest odd integer to the given input, e is the base of the natural logarithm
     */
    private static int P(int w) {
        return switch (w) {
            case 16 -> 0xB7E1;
            case 32 -> 0xB7E15163; //3084996963
            default -> throw new IllegalArgumentException("supported w in {16, 32}");
        };
    }

    /**
     * Odd((&phi - 1) * 2^w),
     * where Odd is the nearest odd integer to the given input, where &phi is the golden ratio
     */
    private static int Q(int w) {
        return switch (w) {
            case 16 -> 0x9E37;
            case 32 -> 0x9E3779B9; //2654435769
            default -> throw new IllegalArgumentException("supported w in {16, 32}");
        };
    }

    private static int lcs(int value, int count) {
//        return (value << (count & 31) | value >> (32 - (count & 31)));
        return (value << count | value >> (32 - count));
    }

    private static int rcs(int value, int count) {
        return (value >> count | value << (Integer.SIZE - count));
    }

    private final int w = 16;            //length of a word in bits
    private final int u = w / 8;         //length of a word in bytes

    private int b;                       //length of a key in bytes
    private byte[] K;                    //key

    private int c;                       //length of the key in words (or 1, if b = 0)
    private int[] L;                     //key in words

    private final int r = 12;             //number of rounds
    private final int t = 2 * (r + 1);   //the number of round subkeys required.
    private int[] S;                     //round subkey words

    public void setup(String key) {
        K = key.getBytes();
        b = K.length;
        c = (int) Math.max(1, Math.ceil(8 * (float) b / w));
        L = new int[c];
        S = new int[t];

        for (int i = b - 1; i > -1; i--) {
            L[i / u] = (L[i / u] << 8) + K[i];
        }

        S[0] = P(w);
        for (var i = 1; i < t; i++) {
            S[i] = S[i - 1] + Q(w);
        }

        int A, B, i, j, k;
        for (A = B = i = j = k = 0; k < 3 * t; k++, i = (i + 1) % t, j = (j + 1) % c) {
            A = S[i] = lcs(S[i] + A + B, 3);
            B = L[j] = lcs(L[j] + A + B, (A + B));
        }
    }

    public int[] encrypt(int[] plaintext) {
        var ciphertext = new int[2];
        var A = plaintext[0] + S[0];
        var B = plaintext[1] + S[1];

        for (var i = 1; i <= r; i++) {
            A = lcs(A ^ B, B) + S[2 * i];
            B = lcs(B ^ A, A) + S[2 * i + 1];
        }

        ciphertext[0] = A;
        ciphertext[1] = B;

        return ciphertext;
    }

    public int[] decrypt(int[] ciphertext) {
        var plaintext = new int[2];
        var A = ciphertext[0];
        var B = ciphertext[1];

        for (var i = r; i > 0; i--) {
            B = rcs(B - S[2 * i + 1], A) ^ A;
            A = rcs(A - S[2 * i], B) ^ B;
        }

        plaintext[0] = A - S[0];
        plaintext[1] = B - S[1];

        return plaintext;
    }

    public static void main(String[] args) {
        var rc5 = new RC5();
        rc5.setup(new String(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}));
        var res = rc5.encrypt(new int[]{0, 0});
        System.out.println(res[0] + " " + Integer.toHexString(res[0]));
        System.out.println(res[1] + " " + Integer.toHexString(res[1]));

        var dec = rc5.decrypt(res);
        System.out.println(dec[0] + " " + Integer.toHexString(dec[0]));
        System.out.println(dec[1] + " " + Integer.toHexString(dec[1]));
    }
}
