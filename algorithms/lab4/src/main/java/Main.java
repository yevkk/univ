import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String EXIT_CMD_STR = "exit";
    static final String SHOW_CMD_STR = "show";
    static final String INSERT_CMD_STR = "insert";
    static final String LOOKUP_CMD_STR = "lookup";
    static final String DELETE_CMD_STR = "delete";
    static final String RESET_CMD_STR = "reset";
    static final String HELP_CMD_STR = "help";

    static BinaryTree tree = new BinaryTree();

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
                    System.out.println(tree.toString());
                    break;
                case INSERT_CMD_STR:
                    int card_no = 0;
                    int year = 0;
                    boolean does_sports = false;
                    if (cmd_args.length >= 6) {
                        try {
                            card_no = Integer.parseInt(cmd_args[1]);
                            year = Integer.parseInt(cmd_args[4]);
                            does_sports = Boolean.parseBoolean(cmd_args[5]);
                            if (card_no < 0 || year < 0 || year > 6) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }
                        var res = tree.insert(new Student(cmd_args[2], cmd_args[3], card_no, year, does_sports));
                        System.out.println(res);
                    } else {
                        System.out.println("Too few arguments");
                    }
                    break;
                case LOOKUP_CMD_STR:
                    var lookup_res = tree.lookup();
                    for (var e : lookup_res) {
                        System.out.println(e.toString());
                    }
                    break;
                case DELETE_CMD_STR:
                    tree.delete();
                    System.out.println("Delete performed");
                    break;
                case RESET_CMD_STR:
                    tree = new BinaryTree();
                    System.out.println("Tree was reset");
                    break;
                case HELP_CMD_STR:
                    System.out.println("\texit");
                    System.out.println("\tshow");
                    System.out.println("\tinsert <card #> <name> <surname> <year> <does_sports>");
                    System.out.println("\tlookup");
                    System.out.println("\tdelete");
                    System.out.println("\treset");
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
            System.out.println();
        }
    }
}
