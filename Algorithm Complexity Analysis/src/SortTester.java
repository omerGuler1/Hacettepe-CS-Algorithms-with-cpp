import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SortTester {

    public static int a=0;

    // These functions measure avg sorting times for different algorithms and input sizes.
    // Modify the return type to return the last sorted array
    public static int[] testInsertionSort(int[] data, int inputSize, ArrayList<Integer> times) {
        int avg = 0;
        int[] lastSortedArray = null; // To hold the last sorted array
        for (int i = 0; i < 10; i++) {
            int[] dataForInsertionSort = Arrays.copyOf(data, inputSize);

            if (a==1)
                Arrays.sort(dataForInsertionSort);
            else if (a==2){
                Arrays.sort(dataForInsertionSort);
                reverse(dataForInsertionSort);
            }

            long start = System.currentTimeMillis();
            InsertionSort.insertionSort(dataForInsertionSort);
            long end = System.currentTimeMillis();
            double duration = (end - start);
            avg += duration;

            lastSortedArray = dataForInsertionSort; // Update with the latest sorted array
        }
        avg = avg / 10;
        times.add(avg);

        System.out.println("insertion sort for " + inputSize + " = " + avg);

        return lastSortedArray; // Return the last sorted array
    }



    public static void testCountingSort(int[] data, int inputSize, ArrayList<Integer> times) {
        int avg = 0;
        for (int i = 0; i < 10; i++) {
            int[] dataForCountingSort = Arrays.copyOf(data, inputSize);

            if (a==1)
                Arrays.sort(dataForCountingSort);
            else if (a==2){
                Arrays.sort(dataForCountingSort);
                reverse(dataForCountingSort);
            }

            long start = System.currentTimeMillis();
            CountingSort.countSort(dataForCountingSort);
            long end = System.currentTimeMillis();
            double duration = (end - start);
            avg += duration;
        }
        avg = avg / 10;
        times.add(avg);

        System.out.println("counting sort for " + inputSize + " = " + avg);
    }


    public static void testMergeSort(int[] data, int inputSize, ArrayList<Integer> times) {
        int avg = 0;
        for (int i = 0; i < 10; i++) {
            int[] dataForMergeSort = Arrays.copyOf(data, inputSize);

            if (a==1)
                Arrays.sort(dataForMergeSort);
            else if (a==2){
                Arrays.sort(dataForMergeSort);
                reverse(dataForMergeSort);
            }

            long start = System.currentTimeMillis();
            MergeSort.mergeSort(dataForMergeSort);
            long end = System.currentTimeMillis();
            double duration = (end - start);
            avg += duration;
        }
        avg = avg / 10;
        times.add(avg);

        System.out.println("merge sort for " + inputSize + " = " + avg);
    }

    public static void reverse (int[] array) {
        for(int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    // plots related graphs with sorting algorithms
    public static int[][] calculateDataForSorting(int[] data, int[] inputSizes) throws IOException {
        String[] sorts = {"Insertion Sort", "Merge Sort", "Counting Sort"};

        ArrayList<Integer> insertionSort = new ArrayList<>();
        ArrayList<Integer> countingSort = new ArrayList<>();
        ArrayList<Integer> mergeSort = new ArrayList<>();



        // random
        double[][] randomYAxis = new double[3][10];
        System.out.println("FOR RANDOM DATA:");

        System.out.println("Insertion Sort");
        for (int size : inputSizes){
            testInsertionSort(data, size, insertionSort);
        }
        for (int i = 0; i < 10; i++) {
            randomYAxis[0][i] = insertionSort.get(i);
        }

        System.out.println("--------------------");

        System.out.println("Merge Sort");
        for (int size : inputSizes){
            testMergeSort(data, size, mergeSort);
        }
        for (int i = 0; i < 10; i++) {
            randomYAxis[1][i] = mergeSort.get(i);
        }


        System.out.println("--------------------");

        System.out.println("Counting Sort");
        for (int size : inputSizes){
            testCountingSort(data, size, countingSort);
        }
        for (int i = 0; i < 10; i++) {
            randomYAxis[2][i] = countingSort.get(i);
        }

        Main.showAndSaveChart("Test on Random Data", sorts, inputSizes, randomYAxis, 1);



        // sorted
        a=1;
        insertionSort.clear();
        countingSort.clear();
        mergeSort.clear();

        double[][] sortedYAxis = new double[3][10];
        System.out.println("FOR SORTED DATA:");

        System.out.println("Insertion Sort");
        int[][] lastSortedData = new int[inputSizes.length][];
        int n = 0;
        for (int size : inputSizes){
            lastSortedData[n] = new int[size];
            lastSortedData[n] = testInsertionSort(data, size, insertionSort);
            n++;
        }
        for (int i = 0; i < 10; i++) {
            sortedYAxis[0][i] = insertionSort.get(i);
        }

        System.out.println("--------------------");

        System.out.println("Merge Sort");
        for (int size : inputSizes){
            testMergeSort(data, size, mergeSort);
        }
        for (int i = 0; i < 10; i++) {
            sortedYAxis[1][i] = mergeSort.get(i);
        }


        System.out.println("--------------------");

        System.out.println("Counting Sort");
        for (int size : inputSizes){
            testCountingSort(data, size, countingSort);
        }
        for (int i = 0; i < 10; i++) {
            sortedYAxis[2][i] = countingSort.get(i);
        }

        Main.showAndSaveChart("Test on Sorted Data", sorts, inputSizes, sortedYAxis, 1);



        // reverse sorted
        a=2;
        insertionSort.clear();
        countingSort.clear();
        mergeSort.clear();

        double[][] reverseSortedYAxis = new double[3][10];
        System.out.println("FOR REVERSE SORTED DATA:");


        System.out.println("Insertion Sort");
        for (int size : inputSizes){
            testInsertionSort(data, size, insertionSort);
        }
        for (int i = 0; i < 10; i++) {
            reverseSortedYAxis[0][i] = insertionSort.get(i);
        }

        System.out.println("--------------------");

        System.out.println("Merge Sort");
        for (int size : inputSizes){
            testMergeSort(data, size, mergeSort);
        }
        for (int i = 0; i < 10; i++) {
            reverseSortedYAxis[1][i] = mergeSort.get(i);
        }


        System.out.println("--------------------");
        System.out.println("Counting Sort");
        for (int size : inputSizes){
            testCountingSort(data, size, countingSort);
        }
        for (int i = 0; i < 10; i++) {
            reverseSortedYAxis[2][i] = countingSort.get(i);
        }

        Main.showAndSaveChart("Test on Reverse Sorted Data", sorts, inputSizes, reverseSortedYAxis, 1);
        return lastSortedData;
    }
}
