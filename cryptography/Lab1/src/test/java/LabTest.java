import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

class LabTest {
    private final int testSize = 10;
    private final BigInteger lowerBound = BigInteger.ONE;
    private final BigInteger upperBound =  new BigInteger("999999999999999999999999999999999999999999");

    @Test
    public void FermatTestNonPrimesTest() {
        assertFalse(Lab.FermatTest(new BigInteger("37846138746128376"), 5));
        assertFalse(Lab.FermatTest(new BigInteger("37846138746128376674382764823764823746"), 5));
        assertFalse(Lab.FermatTest(new BigInteger("87178291199").multiply(new BigInteger("87178291199")), 5));
        assertFalse(Lab.FermatTest(new BigInteger("87178291199").multiply(new BigInteger("265252859812191058636308479999999")), 5));
        assertFalse(Lab.FermatTest(new BigInteger("99194853094755497").multiply(new BigInteger("99194853094755497")), 5));
        assertFalse(Lab.FermatTest(new BigInteger("99194853094755497").multiply(new BigInteger("523347633027360537213687137")), 5));
        assertFalse(Lab.FermatTest(new BigInteger("2").multiply(new BigInteger("523347633027360537213687137")), 5));
    }

    /**
     * Using values from
     * <a href="https://en.m.wikipedia.org/wiki/List_of_prime_numbers">https://en.m.wikipedia.org/wiki/List_of_prime_numbers</a>
     */
    @Test
    public void FermatTestPrimesTest() {
        assertTrue(Lab.FermatTest(new BigInteger("479001599"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("87178291199"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("10888869450418352160768000001"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("265252859812191058636308479999999"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("263130836933693530167218012159999999"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("8683317618811886495518194401279999999"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("99194853094755497"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("1066340417491710595814572169"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("19134702400093278081449423917"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("59604644783353249"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("523347633027360537213687137"), 5));
        assertTrue(Lab.FermatTest(new BigInteger("43143988327398957279342419750374600193"), 5));
    }

    @Test
    public void MillerRabinTestsNonPrimesTest() {
        assertFalse(Lab.MillerRabinTest(new BigInteger("37846138746128376"), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("37846138746128376674382764823764823746"), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("87178291199").multiply(new BigInteger("87178291199")), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("87178291199").multiply(new BigInteger("265252859812191058636308479999999")), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("99194853094755497").multiply(new BigInteger("99194853094755497")), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("99194853094755497").multiply(new BigInteger("523347633027360537213687137")), 5));
        assertFalse(Lab.MillerRabinTest(new BigInteger("2").multiply(new BigInteger("523347633027360537213687137")), 5));
    }

    /**
     * Using values from
     * <a href="https://en.m.wikipedia.org/wiki/List_of_prime_numbers">https://en.m.wikipedia.org/wiki/List_of_prime_numbers</a>
     */
    @Test
    public void MillerRabinTestsPrimesTest() {
        assertTrue(Lab.MillerRabinTest(new BigInteger("479001599"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("87178291199"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("10888869450418352160768000001"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("265252859812191058636308479999999"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("263130836933693530167218012159999999"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("8683317618811886495518194401279999999"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("99194853094755497"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("1066340417491710595814572169"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("19134702400093278081449423917"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("59604644783353249"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("523347633027360537213687137"), 5));
        assertTrue(Lab.MillerRabinTest(new BigInteger("43143988327398957279342419750374600193"), 5));
    }

    @Test
    public void modPowBySquaringTest() {
        var testSize = 10;
        var lowerBound = BigInteger.ONE;
        var upperBound =  new BigInteger("999999999999999999999999999999999999999999");
        int i;

        i = testSize / 2;
        while (i-- > 0) {
            var num = Lab.randomBigInteger(lowerBound, upperBound);
            var mod = Lab.randomBigInteger(lowerBound, upperBound);
            assertEquals(BigInteger.ONE, Lab.modPowBySquaring(num, BigInteger.ZERO, mod));
        }

        i = testSize;
        while (i-- > 0) {
            var num = Lab.randomBigInteger(lowerBound, upperBound);
            var mod = Lab.randomBigInteger(lowerBound, num);
            assertEquals(num, Lab.modPowBySquaring(num, BigInteger.ONE, mod));
        }

        i = testSize;
        while (i-- > 0) {
            var num = Lab.randomBigInteger(lowerBound, upperBound);
            var exp = Lab.randomBigInteger(lowerBound, upperBound);
            var mod = Lab.randomBigInteger(lowerBound, upperBound);
            assertEquals(num.modPow(exp, mod), Lab.modPowBySquaring(num, exp, mod));
        }
    }

    @Test
    public void KaratsubaAlgorithmTest() {
        var i = testSize * 3;
        while (i-- > 0) {
            var A = Lab.randomBigInteger(lowerBound, upperBound);
            var B = Lab.randomBigInteger(lowerBound, upperBound);
            assertEquals(A.multiply(B), Lab.KaratsubaAlgorithm(A, B));
        }
    }
}