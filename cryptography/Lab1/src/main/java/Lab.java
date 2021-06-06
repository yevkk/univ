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
        // (n-1) = t*2^s
        var t = n.subtract(BigInteger.ONE);
        var s = BigInteger.ZERO;
        {
            var divRem = t.divideAndRemainder(BigInteger.TWO);
            while (divRem[1].equals(BigInteger.ZERO)) {
                t = divRem[0];
                s = s.add(BigInteger.ONE);
                divRem = t.divideAndRemainder(BigInteger.TWO);
            }
        }

        A: while (attempts-- > 0) {
           var a = randomBigInteger(BigInteger.TWO, n.subtract(BigInteger.TWO));
           var x = a.modPow(t, n);
           if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
               continue;
           }

           var sCounter = BigInteger.ONE;
           while (sCounter.compareTo(s) < 0) {
               x = x.modPow(BigInteger.TWO, n);
               if (x.equals(n.subtract(BigInteger.ONE))) {
                   continue A;
               }
               sCounter = sCounter.add(BigInteger.ONE);
           }
           return false;
        }

        return true;
    }


    public static void main(String[] args) {
        BigInteger n = new BigInteger("1281");


    }

}
