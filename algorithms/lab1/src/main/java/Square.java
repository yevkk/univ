import java.lang.Math;
import java.util.Random;

public class Square {
    static final double SIDE_LENGTH_RAND_MAX = 100;
    static final double X_RANGE_RAND_MAX = 5000;
    static final double Y_RANGE_RAND_MAX = 5000;

    Point[] points = new Point[4];

    public static class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        void rotate_around_0(double ang_rad) {
            var prev_x = x;
            var prev_y = y;
            this.x = prev_x * Math.cos(ang_rad) - prev_y * Math.sin(ang_rad);
            this.y = prev_x * Math.sin(ang_rad) + prev_y * Math.cos(ang_rad);
        }

        void move(double x, double y) {
            this.x += x;
            this.y += y;
        }
    }

    public Square() {
        var rand = new Random();
        var half_side_length = rand.nextDouble() * SIDE_LENGTH_RAND_MAX / 2 + 1;
        var center = new Point(rand.nextDouble() * X_RANGE_RAND_MAX, rand.nextDouble() * Y_RANGE_RAND_MAX);
        var rotate_deg = rand.nextDouble() * Math.PI / 2;

        // placing a square having center in (0, 0)
        points[0] = new Point(-half_side_length, half_side_length);
        points[1] = new Point(half_side_length, half_side_length);
        points[2] = new Point(half_side_length, -half_side_length);
        points[3] = new Point(-half_side_length, -half_side_length);

        for (var point : points) {
            // rotating a point around (0, 0)
            point.rotate_around_0(rotate_deg);
            // moving a point to a designated position
            point.move(center.x, center.y);
        }

        // taking into account the process of random square generation there is no need to check if the generated
        // figure is actually a square;
    }

    public Point point_get(int n) {
        return (n < 0 || n > 3) ? null : points[n];
    }

    public double side_length() {
        return Math.sqrt(Math.pow(points[0].x - points[1].x, 2) + Math.pow(points[0].y - points[1].y, 2));
    }

    public double perimeter() {
        return 4 * side_length();
    }

    public double square() {
        return Math.pow(side_length(), 2);
    }

    public String toString() {
        return String.format("P(key): %.3f; S: %.3f; (%.3f, %.3f), (%.3f, %.3f), (%.3f, %.3f), (%.3f, %.3f);",
                perimeter(),
                square(),
                points[0].x, points[0].y,
                points[1].x, points[1].y,
                points[2].x, points[2].y,
                points[3].x, points[3].y
        );
    }

}
