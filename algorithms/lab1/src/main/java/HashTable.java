public class HashTable {
    Square[] table;
    int size;

    public HashTable(int size) {
        this.size = size;
        this.table = new Square[size];
    }

    public boolean insert(Square square) {
        var hash = (int) square.perimeter() % size;
        if (table[hash] == null) {
            table[hash] = square;
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        var i_len_max = String.valueOf(size).length();

        res.append(String.format("HASH TABLE (%d)\n", size())).append("-".repeat(50)).append('\n');
        for (int i = 0; i < size; i++) {
            if (table[i] == null) continue;
            var i_len = String.valueOf(i).length();
            res.append(String.valueOf(i)).append(":").append(" ".repeat(i_len_max - i_len + 1));
            res.append(table[i].toString()).append('\n');

//            res.append(" ".repeat(i_len_max + 2));
//            res.append("bbb\n");
        }
        res.append("-".repeat(50));

        return res.toString();
    }
}
