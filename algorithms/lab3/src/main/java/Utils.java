import java.util.Random;

public class Utils {
    public static final int RAND_MAX = 999999999;
    public static final int NUMBER_OF_MEASUREMENTS = 10;
    public static final int SIZE_BASE = 100;

    private static final int SIZE_MAX = 20000;
    private static final int SIZE_STEP = 500;

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

    public static long avSortTime(Sort.Type type, int size) {
        long sum = 0;
        for (int i = 0; i < NUMBER_OF_MEASUREMENTS; i++) {
            var arr = genRandomArr(size);
            sum += sortTime(type, arr);
        }
        return sum / NUMBER_OF_MEASUREMENTS;
    }

    public static void printTimeMeasurements(Sort.Type type) {
        System.out.printf("%s SORT\n--------------------\n", Sort.typeString(type));
        for (int i = 0, size = SIZE_BASE; i < 3; i++, size *= SIZE_BASE) {
            System.out.printf("n = %d\n", size);
            System.out.printf("t: %d\n", avSortTime(type, size));
            System.out.println("--------------------");
        }
        System.out.println();
    }

    public static void printTimeMeasurementsForPlot(Sort.Type type) {
        System.out.printf("%s SORT\n--------------------\n", Sort.typeString(type));
        for (int size = SIZE_STEP; size <= SIZE_MAX; size += SIZE_STEP) {
            System.out.printf("%d, %d\n", size, avSortTime(type, size));
        }
        System.out.println("--------------------\n");
    }
}
