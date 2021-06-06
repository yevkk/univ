import java.math.BigDecimal;
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

        A:
        while (attempts-- > 0) {
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

    /**
     * <b>3</b>
     */
    public static BigInteger modPowBySquaring(BigInteger x, BigInteger n, BigInteger mod) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }

        if (n.equals(BigInteger.ONE)) {
            return x;
        }

        if (n.divideAndRemainder(BigInteger.TWO)[1].equals(BigInteger.ZERO)) {
            return modPowBySquaring(x.modPow(BigInteger.TWO, mod), n.shiftRight(1), mod);
        } else {
            return x.multiply(modPowBySquaring(x.modPow(BigInteger.TWO, mod), n.subtract(BigInteger.ONE).shiftRight(1), mod));
        }
    }

    /**
     * <b>4</b><br/>
     * A = ax + b, B = cx + d <br/>
     * ac = K, bd = M, (a+b)(c+d) = N <br/>
     * AB = Kx^2 = (N - K - M)x + M
     */
    public static BigInteger KaratsubaAlgorithm(BigInteger A, BigInteger B) {
        var fullLength = Math.max(A.bitLength(), B.bitLength());
        var length = fullLength / 2;

        var a = A.shiftRight(length);
        var b = A.subtract(a.shiftLeft(length));

        var c = B.shiftRight(length);
        var d = B.subtract(c.shiftLeft(length));

        var K = a.multiply(c);
        var M = b.multiply(d);
        var N = (a.add(b)).multiply(c.add(d));

        return K.shiftLeft(2 * length).add(N.subtract(K).subtract(M).shiftLeft(length)).add(M);
    }

    /**
     * <b>6</b><br/>
     * ax + by = g<br/>
     * [0] = g, [1] = x, [2] = y
     */
    public static BigInteger[] extendedEuclideanAlgorithm(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }
        var arr = extendedEuclideanAlgorithm(b, a.remainder(b));
        var x = arr[2];
        var y = arr[1].subtract(arr[2].multiply(a.divide(b)));

        return new BigInteger[]{arr[0], x, y};
    }


    public static void main(String[] args) {
//        240 and 46
        var res = extendedEuclideanAlgorithm(new BigInteger("240"), new BigInteger("46"));
        System.out.println(res[1] + " " + res[2] + " " + res[0]);
    }

}
