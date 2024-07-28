public class MergeSort {

    public static int[] mergeSort(int[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }
        int mid = array.length / 2;

        // Split the array into left and right halves
        int[] leftHalf = new int[mid];
        int[] rightHalf = new int[array.length - mid];

        System.arraycopy(array, 0, leftHalf, 0, mid);
        System.arraycopy(array, mid, rightHalf, 0, array.length - mid);

        // Recursively sort both halves
        leftHalf = mergeSort(leftHalf);
        rightHalf = mergeSort(rightHalf);

        // Merge the sorted halves and return the result
        return merge(leftHalf, rightHalf);
    }

    private static int[] merge(int[] leftHalf, int[] rightHalf) {
        int leftSize = leftHalf.length;
        int rightSize = rightHalf.length;
        int[] mergedArray = new int[leftSize + rightSize];

        int i = 0, j = 0, k = 0;

        // Merge arrays until one is empty
        while (i < leftSize && j < rightSize) {
            if (leftHalf[i] <= rightHalf[j]) {
                mergedArray[k++] = leftHalf[i++];
            } else {
                mergedArray[k++] = rightHalf[j++];
            }
        }

        // Copy remaining elements
        while (i < leftSize) {
            mergedArray[k++] = leftHalf[i++];
        }
        while (j < rightSize) {
            mergedArray[k++] = rightHalf[j++];
        }

        return mergedArray;
    }
}