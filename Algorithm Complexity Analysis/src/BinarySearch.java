public class BinarySearch {
    public static int binSearch(int[] A, int x){
        int lo = 0;
        int hi = A.length - 1;
        while ((hi - lo) > 1){
            int mid = (hi + lo) / 2;

            if (A[mid] < x)
                lo = mid+1;
            else
                hi = mid;
        }
        if (A[lo] == x)
            return lo;
        else if (A[hi] == x)
            return hi;
        else
            return -1;
    }
}
