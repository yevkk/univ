public class Task1 {
    private static double f(double x) {
        return Math.sin(Math.sqrt(1 + Math.pow(x, 2) + x));
    }

    public static double rectangleMethod(double range_min, double range_max, double step) {
        double res = 0;
        for (double a = range_min; a < range_max; a += step) {
            double b = Math.min(a + step, range_max);
            res += (b - a) * f ((a + b) / 2);
        }
        return res;
    }

    public static double trapeziumMethod(double range_min, double range_max, double step) {
        double res = 0;
        for (double a = range_min; a < range_max; a += step) {
            double b = Math.min(a + step, range_max);
            res += (b - a) * (f (a) + f(b)) / 2;
        }
        return res;
    }

    public static double simpsonMethod(double range_min, double range_max, double step) {
        double res = 0;
        for (double a = range_min; a < range_max; a += step) {
            double b = Math.min(a + step, range_max);
            res += (b - a) * (f(a) + 4 * f((a + b) / 2) + f(b)) / 6;
        }
        return res;
    }
}
