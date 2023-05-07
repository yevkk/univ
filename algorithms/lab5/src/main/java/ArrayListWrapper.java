import java.util.ArrayList;

public class ArrayListWrapper {
    private final ArrayList<Student> list = new ArrayList<>();
    private int false_counter = 0;

    public int size() {
        return list.size();
    }

    public Student get(int i) {
        return list.get(i);
    }

    private int getKey(int i) {
        return list.get(i).getStudentCardNo();
    }

    public void add(Student student) {
        if (student.hasMilitaryTraining()) {
            list.add(student);
        } else {
            list.add(false_counter, student);
            false_counter++;
        }
    }

    private void swap( int first, int second) {
        var tmp = list.get(first);
        list.set(first, list.get(second));
        list.set(second, tmp);
    }

    private void bubbleSort(int first, int last) {
        for (int i = first; i < last - 1; i++) {
            for (int j = first; j < last - i - 1; j++) {
                if (list.get(j).getStudentCardNo() > list.get(j + 1).getStudentCardNo()) {
                    swap(j, j + 1);
                }
            }
        }
    }

    private Student interpolationSearchInternal(int first, int last, int student_card_no) {
        int pos;
        if ((first <= last) && (student_card_no >= getKey(first)) && (student_card_no <= getKey(last))) {
            pos = first + ((student_card_no - getKey(first)) / (getKey(last) - getKey(first))) * (first - last);

            if (getKey(pos) < student_card_no) {
                return interpolationSearch(pos + 1, last, student_card_no);
            } else if (getKey(pos) > student_card_no) {
                return interpolationSearch(first, pos - 1, student_card_no);
            } else {
                return get(pos);
            }
        }
        return null;
    }

    private Student interpolationSearch(int first, int last, int student_card_no) {
        bubbleSort(first, last);
        return interpolationSearchInternal(first, last, student_card_no);
    }

    public boolean task(int student_card_no) {
        return interpolationSearch(false_counter, list.size() - 1, student_card_no) != null;
    }
}
