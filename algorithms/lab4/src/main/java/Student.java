public class Student {
    private final String name;
    private final String surname;
    private final int student_card_no;
    private final int year;
    private final boolean does_sports;

    public Student(String name, String surname, int student_card_no, int year, boolean does_sports) {
        this.name = name;
        this.surname = surname;
        this.student_card_no = student_card_no;
        this.year = year;
        this.does_sports = does_sports;
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

    public int getYear() {
        return year;
    }

    public boolean doesSports() {
        return does_sports;
    }

    public String toString() {
        return String.format("%d, %s %s, year: %d, does sports: %s", student_card_no, name, surname, year, does_sports);
    }

    public boolean meetsVariantConditions() {
        return (year == 2) && does_sports;
    }
}
