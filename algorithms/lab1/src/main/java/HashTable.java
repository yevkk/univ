import java.util.LinkedList;

public class HashTable {
    public enum CompParam {
        PERIMETER,
        SQUARE
    }

    public enum CompType {
        GREATER,
        SMALLER
    }

    Square[] table;
    LinkedList<Square>[] table_enhanced;
    int size;
    boolean enhanced;

    public HashTable(int size, boolean enhanced) {
        this.size = size;
        this.enhanced = enhanced;
        if (enhanced) {
            this.table_enhanced = new LinkedList[size];
            for (int i = 0; i < size; i++) {
                this.table_enhanced[i] = new LinkedList<>();
            }
        } else {
            this.table = new Square[size];
        }
    }

    public boolean insert(Square square) {
        var hash = (int) square.perimeter() % size;

        if (enhanced) {
            table_enhanced[hash].addLast(square);
        } else {
            if (table[hash] != null) {
                return false;
            }
            table[hash] = square;
        }
        return true;
    }

    public int size() {
        return size;
    }

    void conditionalRemoveInternal(Square square, CompParam param, CompType type, double val) {
        boolean flag = false;
        double value_to_compare;

        switch (param) {
            case PERIMETER:
                value_to_compare = square.perimeter();
                break;
            case SQUARE:
                value_to_compare = square.square();
                break;
            default:
                return;
        }

        switch (type) {
            case GREATER:
                flag = value_to_compare > val;
                break;
            case SMALLER:
                flag = value_to_compare < val;
                break;
            default:
                return;
        }

        if (flag) {
            square.reverseValid();
        }
    }

    public void conditionalRemove(CompParam param, CompType type, double val) {
        for (int i = 0; i < size; i ++) {
            if (enhanced) {
                for (int j = 0; j < table_enhanced[i].size(); j++) {
                    conditionalRemoveInternal(table_enhanced[i].get(j), param, type, val);
                }
            } else {
                conditionalRemoveInternal(table[i], param, type, val);
            }
        }
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        var i_len_max = String.valueOf(size).length();

        res.append(String.format("HASH TABLE (s: %d, e: %s)\n", size(), String.valueOf(enhanced))).append("-".repeat(50)).append('\n');
        for (int i = 0; i < size; i++) {
            if ((enhanced && table_enhanced[i].isEmpty()) || (!enhanced && table[i] == null)) continue;
            var i_len = String.valueOf(i).length();
            res.append(String.valueOf(i)).append(":").append(" ".repeat(i_len_max - i_len + 1));

            if (enhanced) {
                res.append(table_enhanced[i].get(0).toString()).append('\n');
                for (int j = 1; j < table_enhanced[i].size(); j++) {
                    res.append(" ".repeat(i_len_max + 2));
                    res.append(table_enhanced[i].get(j).toString()).append('\n');
                }
            } else {
                res.append(table[i].toString()).append('\n');
            }
        }
        res.append("-".repeat(50));

        return res.toString();
    }
}
