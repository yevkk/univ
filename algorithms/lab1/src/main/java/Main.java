import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String EXIT_CMD_STR = "exit";
    static final String RESET_CMD_STR = "reset";
    static final String CREATE_CMD_STR = "create";
    static final String INSERT_CMD_STR = "insert";
    static final String SHOW_CMD_STR = "show";
    static final String REMOVE_CMD_STR = "remove";
    static final String HELP_CMD_STR = "help";

    static HashTable ht;

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
                case RESET_CMD_STR:
                    ht = null;
                    System.out.println("Hash table was reset");
                    break;
                case CREATE_CMD_STR:
                    boolean enhanced = false;
                    if (ht != null) {
                        System.out.println("Reset current hash table first");
                    } else if (cmd_args.length < 2) {
                        System.out.println("No hash table size provided");
                    } else {
                        int size = 0;
                        try {
                            size = Integer.parseInt(cmd_args[1]);
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }

                        if (cmd_args.length > 2 && (cmd_args[2].equals("Y") || cmd_args[2].equals("y"))) {
                            enhanced = true;
                        }
                        ht = new HashTable(size, enhanced);
                        System.out.printf("Created hash table of size %d, enhanced: %s\n", size, String.valueOf(enhanced));
                    }
                    break;
                case INSERT_CMD_STR:
                    var n = 1;
                    if (ht == null) {
                        System.out.println("No hash table created");
                        break;
                    }

                    if (cmd_args.length > 1) {
                        try {
                            n = Integer.parseInt(cmd_args[1]);
                        } catch (Exception e) {
                            System.out.println("Invalid argument");
                            break;
                        }
                    }
                    for (int i = 0; i < n; i++) {
                        var square = new Square();
                        var res = ht.insert(square);

                        System.out.printf("Insert: %s\n", square.toString());
                        System.out.printf("Result: %s\n", String.valueOf(res));
                    }
                    break;
                case REMOVE_CMD_STR:
                    HashTable.CompParam comp_param;
                    HashTable.CompType comp_type;
                    double value;
                    if (ht == null) {
                        System.out.println("No hash table created");
                        break;
                    }

                    if (cmd_args.length < 4) {
                        System.out.println("Invalid number of arguments");
                        break;
                    }

                    if (cmd_args[1].equals("P")) {
                        comp_param = HashTable.CompParam.PERIMETER;
                    } else if (cmd_args[1].equals("S")) {
                        comp_param = HashTable.CompParam.SQUARE;
                    } else {
                        System.out.println("Invalid param argument");
                        break;
                    }

                    if (cmd_args[2].equals("G")) {
                        comp_type = HashTable.CompType.GREATER;
                    } else if (cmd_args[2].equals("S")) {
                        comp_type = HashTable.CompType.SMALLER;
                    } else {
                        System.out.println("Invalid comp type argument");
                        break;
                    }

                    try {
                        value = Double.parseDouble(cmd_args[3]);
                    } catch (Exception e) {
                        System.out.println("Invalid value argument");
                        break;
                    }

                    ht.conditionalRemove(comp_param, comp_type, value);

                    break;
                case SHOW_CMD_STR:
                    if (ht == null) {
                        System.out.println("No hash table created");
                    } else {
                        System.out.println(ht.toString());
                    }
                    break;
                case HELP_CMD_STR:
                    System.out.println("\texit");
                    System.out.println("\treset");
                    System.out.println("\tcreate <size> <enhanced (Y/y for true)>");
                    System.out.println("\tinsert <number>");
                    System.out.println("\tremove <param P/S> <comp type G/S> <than value>");
                    System.out.println("\tshow");
                    break;
                default:
                    System.out.println("Unknown command, use \"help\"");
            }
            System.out.println();
        }
    }
}
