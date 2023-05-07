import java.util.LinkedList;

public class Sort {
    private static void swap(Student[] arr, int first, int second) {
        var tmp = arr[first];
        arr[first] = arr[second];
        arr[second] = tmp;
    }

    private static void bubbleSortInternal(Student[] arr, int first, int last) {
        for (int i = first; i < last - 1; i++) {
            for (int j = first; j < first + last - i - 1; j++) {
                if (arr[j].getGroupNo() > arr[j + 1].getGroupNo()) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void bubbleSort(Student[] arr) {
        bubbleSortInternal(arr, 0, arr.length);
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
        for (var list : lists) {
            for (var student : list) {
                arr[index] = student;
                index++;
            }
        }
    }

    private static int quickSortPartition(Student[] arr, int first, int last) {
        int middle = (first + last) / 2;

        //sorting first, last and middle
        if (arr[first].getGroupNo() > arr[middle].getGroupNo()) {
            swap(arr, first, middle);
        }
        if (arr[middle].getGroupNo() > arr[last].getGroupNo()) {
            swap(arr, middle, last);
        }
        if (arr[first].getGroupNo() > arr[middle].getGroupNo()) {
            swap(arr, first, middle);
        }

        if (last - first <= 2) return middle;

        swap(arr, middle, last - 1);
        var pivot = last - 1;
        int i = first;
        int j = last - 1;

        while (i < j) {
            while(arr[i].getGroupNo() < arr[pivot].getGroupNo()) {
                i++;
                if (i == last) break;
            }
            while(arr[j].getGroupNo() >= arr[pivot].getGroupNo()) {
                j--;
                if (j == 0) break;
            }
            if (i < j) {
                swap(arr, i, j);
            }
        }

        swap(arr, i, pivot);
        return i;
    }

    private static void quickSortInternal(Student[] arr, int first, int last) {
        if (first < last) {
            int div = quickSortPartition(arr, first, last);
            quickSortInternal(arr, first, div - 1);
            quickSortInternal(arr, div + 1, last);
        }
    }

    public static void quickSort(Student[] arr) {
        quickSortInternal(arr, 0, arr.length - 1);
    }
}
