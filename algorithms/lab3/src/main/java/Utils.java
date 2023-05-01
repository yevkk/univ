import java.util.Random;

public class Utils {
    public static final int RAND_MAX = 999999999;
    public static final int NUMBER_OF_MEASUREMENTS = 10;
    public static final int SIZE_BASE = 100;

    public static final Random rand = new Random();

    public static int[] genRandomArr(int size) {
        var res = new int[size];
        for (int i = 0; i < res.length; i++) {
            res[i] = rand.nextInt(RAND_MAX);
        }

        return res;
    }

    public static void printArray(int[] arr) {
        for (int j : arr) {
            System.out.print(j);
            System.out.print(' ');
        }
        System.out.println();
    }

    public static long sortTime(Sort.Type type, int[] arr) {
        long end, start;
        start = System.nanoTime();
        switch (type) {
            case MERGE -> Sort.mergeSort(arr);
            case SHELL -> Sort.shellSort(arr, false);
            case SHELL_KNUTH -> Sort.shellSort(arr, true);
            case QUICK -> Sort.quickSort(arr);
        }
        end = System.nanoTime();
        return end - start;
    }

    private static long printTimeMeasurementsInternal(Sort.Type type, int size) {
        long sum = 0;
        for (int i = 0; i < NUMBER_OF_MEASUREMENTS; i++) {
            var arr = genRandomArr(size);
            var time = sortTime(type, arr);
            System.out.println(time);
            sum += time;
        }
        return sum;
    }

    public static void printTimeMeasurements(Sort.Type type) {
        System.out.printf("%s SORT\n--------------------\n", Sort.typeString(type));
        for (int i = 0, size = SIZE_BASE; i < 3; i++, size *= SIZE_BASE) {
            System.out.printf("n = %d\n", size);
            var sum = Utils.printTimeMeasurementsInternal(type, size);
            System.out.printf("av: %d\n", sum / NUMBER_OF_MEASUREMENTS);
            System.out.println("--------------------");
        }
    }
}
