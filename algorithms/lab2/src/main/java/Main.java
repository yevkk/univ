import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String EXIT_CMD_STR = "exit";
    static final String SHOW_CMD_STR = "show";
    static final String ARR_CMD_STR = "arr";
    static final String SORT_CMD_STR = "sort";
    static final String HELP_CMD_STR = "help";

    static Student[] arr;

    public static void main(String[] args) throws IOException {
        var     reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;

        while (!exit) {
            System.out.print("> ");
            String cmd_line = reader.readLine();
            String[] cmd_args = cmd_line.split("\\s+");

            switch (cmd_args[0]) {
                case EXIT_CMD_STR:
                    exit = true;
                    break;
                case SHOW_CMD_STR:
                    if (arr == null) {
                        System.out.println("Array is null");
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            System.out.printf("%d) %s\n", i + 1, arr[i].toString());
                        }
                    }
                    break;
                case ARR_CMD_STR:
                    int size = 0;
                    if (cmd_args.length > 1) {
                        try {
                            size = Integer.parseInt(cmd_args[1]);
                            if (size < 0) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }
                    }
                    arr = new Student[size];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = Student.genRandom(arr);
                    }
                    System.out.printf("Created array of %d elements\n", arr.length);
                    break;
                case SORT_CMD_STR:
                    int type = 0;
                    if (cmd_args.length > 1) {
                        try {
                            type = Integer.parseInt(cmd_args[1]);
                            if (type < 1 || type > 3) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }
                    }
                    switch (type) {
                        case 1 -> {
                            Sort.bubbleSort(arr);
                            System.out.println("Bubble sort executed");
                        }
                        case 2 -> {
                            Sort.indexSort(arr);
                            System.out.println("Index sort executed");
                        }
                        case 3 -> {
                            Sort.quickSort(arr);
                            System.out.println("Quick sort executed");
                        }
                    }
                    break;
                case HELP_CMD_STR:
                    System.out.println("\texit");
                    System.out.println("\tshow");
                    System.out.println("\tarr <size>");
                    System.out.println("\tsort <method 1-bubble, 2-index, 3-quick>");
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
            System.out.println();
        }
    }
}
