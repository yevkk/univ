import java.util.Random;

public class Student {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MAX_GROUP_NO = 15;
    private static final int MAX_CARD_NO = 999999999;

    private final String name;
    private final String surname;
    private final int student_card_no;
    private final int group_no;

    public Student(String name, String surname, int student_card_no, int group_no) {
        this.name = name;
        this.surname = surname;
        this.student_card_no = student_card_no;
        this.group_no = group_no;
    }

    public static Student genRandom(Student[] students) {
        var rand = new Random();
        var flag = false;

        var name = String.format("%s.", ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        var surname = String.format("%s.", ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        int group_no = rand.nextInt(MAX_GROUP_NO) + 1;
        int student_card_no = 0;

        while (!flag) {
            student_card_no = rand.nextInt(MAX_CARD_NO) + 1;
            flag = true;
            if (students == null) break;
            for (var student : students) {
                if (student == null) continue;
                if (student_card_no == student.getStudentCardNo()) {
                    flag = false;
                    break;
                }
            }
        }

        return new Student(name, surname, student_card_no, group_no);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getStudentCardNo() {
        return student_card_no;
    }

    public int getGroupNo() {
        return group_no;
    }

    public String toString() {
        return String.format("%s %s, card: %d, group: %d", name, surname, student_card_no, group_no);
    }
}
