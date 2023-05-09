public class Task3 {
    private static double f(double x, double y) {
        return 1 / (2 * x - Math.pow(y, 2));
    }

    public static void solve(double x0, double y0, double x_max, double step) {
        var x = x0;
        var y = y0;
        double y_n = 0.0;
        int i = 0;
        System.out.printf("%d x: %f, y: %f\n", i, x, y);

        while (x + step <= x_max) {
            y_n = y + step * f(x, y);
            y = y + step * (f(x, y) + f(x + step, y_n)) / 2;

            x += step;
            i++;

            System.out.printf("%d x: %f, y: %f\n", i, x, y);
        }
    }
}
