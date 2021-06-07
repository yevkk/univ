import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class DiffieHellman {
    public static BigInteger randomBigInteger(BigInteger lowerBound, BigInteger upperBound) {
        upperBound = upperBound.subtract(lowerBound);
        var randNum = upperBound.add(BigInteger.TEN);
        while (randNum.compareTo(upperBound) >= 0) {
            randNum = new BigInteger(upperBound.bitLength(), new Random(System.currentTimeMillis()));
        }
        return randNum.add(lowerBound);
    }

    static class Participant implements Runnable {
        private final Exchanger<BigInteger> exchanger;
        private final BigInteger g;
        private final BigInteger p;
        private BigInteger K;
        private final AtomicBoolean ready = new AtomicBoolean(false);
        private final Logger logger = Logger.getLogger(Thread.currentThread().getName());

        public BigInteger getK() throws InterruptedException {
            if (!ready.get()) {
                Thread.sleep(100);
            }
            return K;
        }

        public Participant(Exchanger<BigInteger> exchanger, BigInteger g, BigInteger p) {
            this.exchanger = exchanger;
            this.g = g;
            this.p = p;
        }

        @Override
        public void run() {
            var num = randomBigInteger(BigInteger.ONE, new BigInteger("9999999999999"));
            var openKey = g.modPow(num, p);

            BigInteger other = BigInteger.ONE;
            try {
                other = exchanger.exchange(openKey);
            } catch (InterruptedException e) {
                logger.warning("participant crashed");
                return;
            }

            K = other.modPow(num, p);

            ready.set(true);
        }
    }

    public Participant[] simulateRandom(BigInteger g, BigInteger p) {
        var lowerBound = BigInteger.ONE;
        var upperBound = new BigInteger("9999999999999");
        return simulate(randomBigInteger(lowerBound, upperBound), randomBigInteger(lowerBound, upperBound));
    }

    public Participant[] simulate(BigInteger g, BigInteger p) {
        var exchanger = new Exchanger<BigInteger>();
        var participants = new Participant[]{
                new Participant(exchanger, g, p),
                new Participant(exchanger, g, p),
        };
        new Thread(participants[0], "Alice").start();
        new Thread(participants[1], "Bob").start();

        return participants;
    }
}
