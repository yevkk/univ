import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // No meaning, used here because when measurement is called first time it gives significant overhead
        Utils.sortTime(Sort.Type.MERGE, Utils.genRandomArr(10));

        // Level 1
        Utils.printTimeMeasurements(Sort.Type.MERGE);

        // Level 2
        Utils.printTimeMeasurements(Sort.Type.SHELL);
        Utils.printTimeMeasurements(Sort.Type.SHELL_KNUTH);

        // Level 3
        Utils.printTimeMeasurements(Sort.Type.QUICK);

        // Level 1
        Utils.printTimeMeasurementsForPlot(Sort.Type.MERGE);

        // Level 2
        Utils.printTimeMeasurementsForPlot(Sort.Type.SHELL);
        Utils.printTimeMeasurementsForPlot(Sort.Type.SHELL_KNUTH);
    }
}
