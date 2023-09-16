import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainBST {
    static final String EXIT_CMD_STR = "exit";
    static final String SHOW_CMD_STR = "show";
    static final String INSERT_CMD_STR = "insert";
    static final String SEARCH_CMD_STR = "search";
    static final String RESET_CMD_STR = "reset";
    static final String HELP_CMD_STR = "help";

    static BinarySearchTree bst = new BinarySearchTree();

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
                    System.out.println(bst);
                    break;
                case INSERT_CMD_STR:
                    int card_no = 0;
                    boolean military_training = false;
                    if (cmd_args.length >= 5) {
                        try {
                            card_no = Integer.parseInt(cmd_args[3]);
                            military_training = Boolean.parseBoolean(cmd_args[4]);
                            if (card_no < 0) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }
                        bst.insert(new Student(cmd_args[1], cmd_args[2], card_no, military_training));
                        System.out.println("Element inserted");
                    } else {
                        System.out.println("Too few arguments");
                    }
                    break;
                case SEARCH_CMD_STR:
                    if (cmd_args.length >= 2) {
                        var res = bst.search(cmd_args[1]);
                        System.out.println(res);
                    } else {
                        System.out.println("Too few arguments");
                    }
                    break;
                case RESET_CMD_STR:
                    bst = new BinarySearchTree();
                    System.out.println("Tree was reset");
                    break;
                case HELP_CMD_STR:
                    System.out.println("\texit");
                    System.out.println("\tshow");
                    System.out.println("\tinsert <name> <surname> <card #> <military training>");
                    System.out.println("\tsearch <name>");
                    System.out.println("\treset");
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
            System.out.println();
        }
    }
}
