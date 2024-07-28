import java.util.ArrayList;
import java.util.Random;

public class Search {

    // the following 2 functions calculate the avg time spent for sorting on the input size given for different searching algorithms
    public static void testBinarySearch(int[] data, int inputSize, ArrayList<Integer> times) {
        int avg = 0;
        for (int i = 0; i < 1000; i++) {
            // to randomly select the number to search
            Random random = new Random();
            int randomIndex = random.nextInt(inputSize);
            long start = System.nanoTime();
            BinarySearch.binSearch(data, data[randomIndex]);
            long end = System.nanoTime();
            double duration = (end - start);
            avg += duration;
        }
        avg = avg / 1000;
        times.add(avg);

        System.out.println("binary search " + inputSize + " = " + avg);
    }

    public static void testLinearSearch(int[] data, int inputSize, ArrayList<Integer> times) {
        int avg = 0;
        for (int i = 0; i < 1000; i++) {
            // to randomly select the number to search
            Random random = new Random();
            int randomIndex = random.nextInt(inputSize);
            long start = System.nanoTime();
            LinearSearch.linearSearch(data, data[randomIndex]);
            long end = System.nanoTime();
            double duration = (end - start);
            avg += duration;
        }
        avg = avg / 1000;
        times.add(avg);

        System.out.println("linear search " + inputSize + " = " + avg);
    }
}