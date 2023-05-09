public class Task2 {
    private static double err = 1e-7;
    private static final int ITERATIONS_MAX = 1000;

    public static void setErr(double err) {
        Task2.err = err;
    }

    public static double getErr() {
        return Task2.err;
    }

    private static double f(double x) {
        return Math.pow(2, x) + 2 * Math.pow(x, 2) - 3;
    }

    private static double f_d(double x) {
        return Math.pow(2, x) * Math.log(2) + 4 * x;
    }

    public static Double bisectionMethod(double range_min, double range_max) {
        if (f(range_min) * f(range_max) > 0) {
            return null;
        }
        double mid = (range_min + range_max) / 2;
        if (Math.abs(f(mid)) < err) {
            return mid;
        }
        if (f(range_min) * f(mid) < 0) {
            return bisectionMethod(range_min, mid);
        } else {
            return bisectionMethod(mid, range_max);
        }
    }

    public static Double tangentMethod(double range_min, double range_max) {
        double iterations_counter = 0;
        double prev = (range_min + range_max) / 2;
        double x = prev - f(prev) / f_d(prev);

        while (Math.abs(x - prev) > err) {
            prev = x;
            x = prev - f(prev) / f_d(prev);
            iterations_counter++;

            if (iterations_counter >= ITERATIONS_MAX) {
                return null;
            }
        }
        return x;
    }

    public static Double secantMethod(double range_min, double range_max) {
        double iterations_counter = 0;
        double x0 = (range_max - range_min) / 2;
        double prev = range_max;
        double x = prev - (f(prev) * (prev - x0)) / (f(prev) - f(x0));

        while (Math.abs(x - prev) > err) {
            prev = x;
            x = prev - (f(prev) * (prev - x0)) / (f(prev) - f(x0));
            iterations_counter++;

            if (iterations_counter >= ITERATIONS_MAX) {
                return null;
            }
        }
        return x;
    }
}
