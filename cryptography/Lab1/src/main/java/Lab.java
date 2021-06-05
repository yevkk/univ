import java.math.BigInteger;
import java.util.Random;

public class Lab {
    public static BigInteger randomBigInteger(BigInteger lowerBound, BigInteger upperBound) {
        upperBound = upperBound.subtract(lowerBound);
        var randNum = upperBound.add(BigInteger.TEN);
        while (randNum.compareTo(upperBound) >= 0) {
            randNum = new BigInteger(upperBound.bitLength(), new Random(System.currentTimeMillis()));
        }
        return randNum.add(lowerBound);
    }

    /**
     * <b>1</b>
     */
    public static boolean FermatTest(BigInteger n, int attempts) {
        while (attempts-- > 0) {
            if (!FermatTestIteration(n)) {
                return false;
            }
        }
        return true;
    }

    private static boolean FermatTestIteration(BigInteger n) {
        var a = randomBigInteger(BigInteger.ONE, n.subtract(BigInteger.ONE));
        var rem = a.modPow(n.subtract(BigInteger.ONE), n);
        return rem.compareTo(BigInteger.ONE) == 0;
    }

    /**
     * <b>2</b>
     */
    public static boolean MillerRabinTest(BigInteger n, int attempts) {

        return true;
    }


    public static void main(String[] args) {

    }

}
