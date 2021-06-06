import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
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

    @Test
    public void extendedEuclideanAlgorithmTest() {
        @AllArgsConstructor
        class TestData {
            final BigInteger a;
            final BigInteger b;
            final BigInteger g;
            final BigInteger x;
            final BigInteger y;

            TestData(String aStr, String bStr, String gStr, String xStr, String yStr) {
                this(new BigInteger(aStr), new BigInteger(bStr), new BigInteger(gStr), new BigInteger(xStr), new BigInteger(yStr));
            }
        };

        TestData[] testCases = new TestData[] {
                new TestData("240", "46", "2", "-9", "47"),
                new TestData("41846453336454588", "232816682494205655162", "106930686", "-337267062871", "60620357"),
                new TestData("69621693624072", "12187710219337815294", "246246246", "7034984142", "-40187"),
                new TestData("23424132314234", "14124312312", "2", "-2613584867", "4334438122490"),
                new TestData("23424132314233", "14124312312", "1", "2819780761", "-4676398835126"),
                new TestData("4170633856389", "813007002916159912991838708", "567567", "194392205428451127723", "-997210"),
                new TestData("5687367407804832715989251503362", "1637007531226911205963628776646", "13264536713726", "8802521658741135", "-30582128569010864"),
                new TestData("99999999989999990000000001", "77777769999999992222223", "9999999999999999", "2097653", "-2696982698"),
        };

        for (TestData testCase : testCases) {
            var res = Lab.extendedEuclideanAlgorithm(testCase.a, testCase.b);
            assertEquals(testCase.g, res[0]);
            assertEquals(testCase.x, res[1]);
            assertEquals(testCase.y, res[2]);
        }
    }
}