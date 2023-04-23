import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String SHOW_CMD_STR = "show";
    static final String EXIT_CMD_STR = "exit";

    public static void main(String[] args) throws IOException {
        var     reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;

        while (!exit) {
            String cmd_line = reader.readLine();
            String[] cmd_args = cmd_line.split("\\s+");

            switch (cmd_args[0]) {
                case SHOW_CMD_STR:
                    System.out.println("1");
                    break;
                case "test":
                    System.out.println("2");
                    break;
                case EXIT_CMD_STR:
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
        }
    }
}
