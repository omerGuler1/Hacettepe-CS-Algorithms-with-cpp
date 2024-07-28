public class CountingSort {
    public static void countSort(int[] array) {
        if (array.length == 0) {
            return;
        }

        int k = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > k) {
                k = array[i]; // Finding the maximum value in the array
            }
        }

        int[] count = new int[k + 1]; // Initialize the count array with the size of k + 1
        int size = array.length;
        int[] output = new int[size];

        // Initialize count array with 0
        for (int i = 0; i <= k; ++i) {
            count[i] = 0;
        }

        // Store the count of each element
        for (int i = 0; i < size; i++) {
            count[array[i]]++;
        }

        // Change count[i] so that count[i] now contains the actual position of this element in the output array
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        // Build the output character array
        for (int i = size - 1; i >= 0; i--) {
            output[count[array[i]] - 1] = array[i];
            count[array[i]]--;
        }

    }
}
