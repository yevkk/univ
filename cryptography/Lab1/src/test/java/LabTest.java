import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

class LabTest {
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
}