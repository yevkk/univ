public class Student {
    private final String name;
    private final String surname;
    private final int student_card_no;
    private final boolean military_training;

    public Student(String name, String surname, int student_card_no, boolean military_training) {
        this.name = name;
        this.surname = surname;
        this.student_card_no = student_card_no;
        this.military_training = military_training;
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

    public boolean hasMilitaryTraining() {
        return military_training;
    }

    public String toString() {
        return String.format("%s %s, card#: %d, mil.training: %s", name, surname, student_card_no, military_training);
    }
}
