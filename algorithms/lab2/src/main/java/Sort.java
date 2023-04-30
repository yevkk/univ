import java.util.LinkedList;

public class Sort {
    public static void bubbleSort(Student[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].getGroupNo() > arr[j + 1].getGroupNo()) {
                    var tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    public static void indexSort(Student[] arr) {
        int size = 0;
        for (var student : arr) {
            if (student.getGroupNo() > size) {
                size = student.getGroupNo();
            }
        }

        LinkedList<Student>[] lists = new LinkedList[size + 1];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<Student>();
        }

        for (var student : arr) {
            int i;
            for (i = 0; i < lists[student.getGroupNo()].size(); i++) {
                if (lists[student.getGroupNo()].get(i).getStudentCardNo() >= student.getStudentCardNo()) {
                    break;
                }
            }
            lists[student.getGroupNo()].add(i, student);
        }

        int index = 0;
        for (int i = 0; i < lists.length; i++) {
            for (var student : lists[i]) {
                arr[index] = student;
                index++;
            }
        }
    }
}
