import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DiffieHellmanTest {
    public final int testSize = 40;
    private final Logger logger = Logger.getLogger(DiffieHellmanTest.class.getName());

    @Test
    public void test() {
        var i = testSize;
        while (i-- > 0) {
            var participants = DiffieHellman.simulateRandom();
            try {
                var KA = participants[0].getK();
                var KB = participants[1].getK();
                assertEquals(KA, KB);
                logger.info("KA: " + KA + ", KB: " + KB);
            } catch (InterruptedException e) {
                logger.warning("simulation crashed");
            }
        }
    }
}