public class Sort {
    private static void swap(int[] arr, int first, int second) {
        var tmp = arr[first];
        arr[first] = arr[second];
        arr[second] = tmp;
    }

    // merge sort
    private static void merge(int[] arr, int first, int middle, int last) {
        int[] left = new int[middle - first + 1];
        int[] right = new int[last - middle];

        System.arraycopy(arr, first, left, 0, left.length);
        System.arraycopy(arr, middle + 1, right, 0, right.length);

        int i = 0;
        int j = 0;
        int k = first;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < left.length) {
            arr[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            arr[k] = right[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int[] arr) {
        for (int sub_size = 1; sub_size < arr.length; sub_size *= 2) {
            for (int first = 0; first < arr.length - 1; first += 2 * sub_size) {
                int middle = Math.min(first + sub_size - 1, arr.length - 1);
                int last = Math.min(first + 2 * sub_size - 1, arr.length - 1);
                merge(arr, first, middle, last);
            }
        }
    }

    // shell sort
    public static void shellSort(int[] arr, boolean use_knuth_seq) {
        int gap = use_knuth_seq ? 1 : arr.length / 2;

        if (use_knuth_seq) {
            while (gap < arr.length) {
                gap = gap * 3 + 1;
            }
        }

        while (gap > 0) {
            for (int i = gap; i < arr.length; i+=1) {
                int tmp = arr[i];
                int j = 0;
                for (j = i; j>= gap && arr[j - gap] > tmp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }

            gap /= use_knuth_seq ? 3 : 2;
        }
    }

    // quick sort
    private static int[] quickSortPartition(int[] arr, int first, int last) {
        if (last - first <= 1) {
            if (arr[last] < arr[first]) {
                swap(arr, first, last);
            }
            return new int[]{first, last};
        }

        int i = first;
        int j = last;
        int middle = first;
        int pivot_val = arr[last];
        while (middle <= last) {
            if (arr[middle] < pivot_val) {
                swap(arr, first++, middle++);
            } else if (arr[middle] == pivot_val) {
                middle++;
            } else if (arr[middle] > pivot_val) {
                swap(arr, middle, last--);
            }

            i = first - 1;
            j = middle;
        }

        return new int[]{i, j};
    }

    private static void quickSortInternal(int[] arr, int first, int last) {
        if (first < last) {
            var ret = quickSortPartition(arr, first, last);
            quickSortInternal(arr, first, ret[0]);
            quickSortInternal(arr, ret[1], last);
        }
    }

    public static void quickSort(int[] arr) {
        quickSortInternal(arr, 0, arr.length - 1);
    }
}
