import java.lang.invoke.StringConcatFactory;

public class Student {
    String name;
    String surname;
    int student_card_no;
    int group_no;

    public Student(String name, String surname, int student_card_no, int group_no) {
        this.name = name;
        this.surname = surname;
        this.student_card_no = student_card_no;
        this.group_no = group_no;
    }

    static Student genRandom() {
        //TODO: implement this
        return new Student("", "", 0, 0);
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
