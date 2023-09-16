import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String EXIT_CMD_STR = "exit";
    static final String TASK1_CMD_STR = "task1";
    static final String TASK2_CMD_STR = "task2";
    static final String TASK3_CMD_STR = "task3";
    static final String HELP_CMD_STR = "help";

    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;

        while (!exit) {
            System.out.print("> ");
            String cmd_line = reader.readLine();
            String[] cmd_args = cmd_line.split("\\s+");
            double range_min, range_max, step, x0, y0, x_max;

            switch (cmd_args[0]) {
                case EXIT_CMD_STR:
                    exit = true;
                    break;
                case TASK1_CMD_STR:
                    if (cmd_args.length < 4) {
                        System.out.println("Too few arguments");
                        break;
                    }
                    try {
                        range_min = Double.parseDouble(cmd_args[1]);
                        range_max = Double.parseDouble(cmd_args[2]);
                        step = Double.parseDouble(cmd_args[3]);
                    } catch (Exception e) {
                        System.out.println("Invalid argument");
                        break;
                    }
                    System.out.printf("Rectangle method: %f\n", Task1.rectangleMethod(range_min, range_max, step));
                    System.out.printf("Trapezium method: %f\n", Task1.trapeziumMethod(range_min, range_max, step));
                    System.out.printf("Simpson method: %f\n", Task1.simpsonMethod(range_min, range_max, step));
                    break;
                case TASK2_CMD_STR:
                    if (cmd_args.length < 3) {
                        System.out.println("Too few arguments");
                        break;
                    }
                    try {
                        range_min = Double.parseDouble(cmd_args[1]);
                        range_max = Double.parseDouble(cmd_args[2]);
                    } catch (Exception e) {
                        System.out.println("Invalid argument");
                        break;
                    }
                    System.out.printf("Err: %e\n", Task2.getErr());
                    System.out.printf("Bisection method: %f\n", Task2.bisectionMethod(range_min, range_max));
                    System.out.printf("Tangent method: %f\n", Task2.tangentMethod(range_min, range_max));
                    System.out.printf("Secant method: %f\n", Task2.secantMethod(range_min, range_max));
                    break;
                case TASK3_CMD_STR:
                    if (cmd_args.length < 5) {
                        System.out.println("Too few arguments");
                        break;
                    }
                    try {
                        x0 = Double.parseDouble(cmd_args[1]);
                        y0 = Double.parseDouble(cmd_args[2]);
                        x_max = Double.parseDouble(cmd_args[3]);
                        step = Double.parseDouble(cmd_args[4]);
                    } catch (Exception e) {
                        System.out.println("Invalid argument");
                        break;
                    }
                    Task3.solve(x0, y0, x_max, step);
                    break;
                case HELP_CMD_STR:
                    System.out.println("\texit");
                    System.out.println("\ttask1 <range min> <range max> <step>");
                    System.out.println("\ttask2 <range min> <range max>");
                    System.out.println("\ttask3 <x_0> <y_0> <x_max> <step>");
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
            System.out.println();
        }
    }
}
