import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * Private Queue Node
 */
class PQNode implements Comparable<PQNode> {

    // **** members ****
    int ai;                 // array index
    int ei;                 // element index in array
    int val;                // element value

    /**
     * Constructor
     */
    public PQNode(int ai, int ei, int val) {
        this.val    = val;
        this.ai     = ai;
        this.ei     = ei;
    }

    /**
     * compareTo() method
     */
    @Override
    public int compareTo(PQNode node) {
        return this.val - node.val;
    }

    /**
     * toString() method
     */
    @Override
    public String toString() {
        return "" + val + " [" + ai + "," + ei + "]";
    }
}


/**
 * 
 */
public class MergeKSortedArrays {


    // **** sorted array size (used by merge & divide) ****
    static int N = 4;


    /**
     * Perform merge operation.
     */
    static void merge(int left, int right, int[] output) {

        // **** store starting point of left and right array ****
        int leftIn  = left * N;
        int rightIn = ((left + right) / 2 + 1) * N;

        // **** store the size of the left and right array ****
        int leftSize    = ((left + right) / 2 - left + 1) * N;
        int rightSize   = (right - (left + right) / 2) * N;

        // **** arrays to temporarily store left and right arrays ****
        int leftArr[]   = new int[leftSize];
        int rightArr[]  = new int[rightSize];
 
        // **** store data in left array ****
        for (int i = 0; i < leftSize; i++)
            leftArr[i] = output[leftIn + i];

        // **** store data in right array ****
        for (int i = 0; i < rightSize; i++)
            rightArr[i] = output[rightIn + i];

        // **** store the current index of temporary left and right array ****
        int leftCurr    = 0;
        int rightCurr   = 0;

        // **** store the current index for the output array
        int in  = leftIn;

        // **** two pointer merge for two sorted arrays ****
        while (leftCurr + rightCurr < leftSize + rightSize) {
            if (rightCurr == rightSize || (leftCurr != leftSize && leftArr[leftCurr] < rightArr[rightCurr])) {
                output[in] = leftArr[leftCurr];
                leftCurr++;
                in++;
            } else {
                output[in] = rightArr[rightCurr];
                rightCurr++;
                in++;
            }
        }
    }


    /**
     * Build recursion tree.
     */
    static void divide(int left, int right, int[] output, int[][] arrs) {

        // **** base case ****
        if (left == right) {

            // **** copy an input to the output array ****
            for (int i = 0; i < N; i++)
                output[left * N + i] = arrs[left][i];

            // **** nothing else to do ****
            return;
        }

        // **** sort left half ****
        divide(left, (left + right) / 2, output, arrs);

        // **** sort right half ****
        divide((left + right) / 2 + 1, right, output, arrs);

        // **** merge left and right half ****
        merge(left, right, output);
    }


    /**
     * Merge sorted arrays of the same length.
     * Recursive entry point.
     * 
     * Execution:  O(nk log(k))
     */
    static int[] mergeSameLength(int[][] arrs) {

        // **** sanity checks(s) ****
        if (arrs == null)
            return null;

        // **** initialization ****
        int k           = arrs.length;
        N               = arrs[0].length;
        int[] output    = new int[N * k];

        // **** divide and merge ****
        divide(0, k - 1, output, arrs);

        // **** return merged array ****
        return output;
    }


    /**
     * Utility method to get the next row and index of
     * smallest element in int[][] arrs.
     * 
     * Runtime:  O(k)
     */
    static int[] nextElement1(int[] ndex, int[][] arrs) {

        // **** initialization ****
        int val     = Integer.MAX_VALUE;
        int[] ai    = new int[2];

        // **** traverse ndex looking for smallest value O(k) ****
        for (int i = 0; i < ndex.length; i++) {

            // **** skip this array (if needed) ****
            if (ndex[i] < 0)
                continue;

            // **** check if this value is smaller ****
            if (arrs[i][ndex[i]] < val) {
                val = arrs[i][ndex[i]];
                ai[0] = i;
                ai[1] = ndex[i];
            }
        }

        // **** update the ndex array ****
        ndex[ai[0]] = ai[1] + 1;
        if (ndex[ai[0]] >= arrs[ai[0]].length)
            ndex[ai[0]] = -1;

        // **** return the pair of values ****
        return ai;
    }


    /**
     * Merges a set of n sorted arrays of different lengths.
     * 
     * Runtime: O(nk * k)
     */
    static int[] merge1(int[][] arrs) {

        // **** sanity check(s) ****
        if (arrs == null)
            return null;

        // **** initialization ****
        int sortLen = 0;
        for (int i = 0; i < arrs.length; i++)
            sortLen += arrs[i].length;
        int[] output    = new int[sortLen];

        int[] ndex      = new int[arrs.length];

        // **** loop until we processed all entries O(nk * k) ****
        for (int i = 0; i < sortLen; i++) {

            // **** select array and index O(k) ****
            int[] ai = nextElement1(ndex, arrs);

            // **** insert into output array O(1) ****
            output[i] = arrs[ai[0]][ai[1]];
        }

        // **** return sorted array ****
        return output;
    }


    /**
     * Copy all arrays and sort the new array.
     * 
     * Arrays.sort() is based on the TimSort algorithm, 
     * giving us a time complexity of O(n log(n)).
     * TimSort makes use of the Insertion sort and the MergeSort algorithms.
     * 
     * Runtime: O(n*k log(n*k))
     */
    static int[] copySort(int[][] arrs) {

        // **** sanity checks ****
        if (arrs == null)
            return null;

        // **** initialization O(n) ****
        int len = 0;
        for (int i = 0; i < arrs.length; i++)
            len += arrs[i].length;

        int[] output = new int[len];

        // **** copy arrays O(n * k) ****
        for (int i = 0, destPos = 0; i < arrs.length; i++) {
            System.arraycopy(arrs[i], 0, output, destPos, arrs[i].length);
            destPos += arrs[i].length;
        }

        // **** sort output array O(n*k log(n*k)) ****
        Arrays.sort(output);

        // **** return sorted array ****
        return output;
    }


    /**
     * Merges a set of n sorted arrays of different lengths.
     * 
     * Runtime: O(n*k log(n*k))
     */
    static int[] merge2(int[][] arrs) {

        // **** sanity check(s) ****
        if (arrs == null)
            return null;

        // **** initialization ****
        PriorityQueue<PQNode> pq = new PriorityQueue<PQNode>();

        int len = 0;
        for (int i = 0; i < arrs.length; i++) {

            // **** skip empty array ****
            if (arrs[i].length == 0)
                continue;

            // **** increment len ****
            len += arrs[i].length;

            // **** add node to pq ****
            pq.add(new PQNode(i, 0, arrs[i][0]));
        }

        int[] output = new int[len];

        // **** loop until we process all elements O(n*k log(n*k)) ****
        for (int i = 0; !pq.isEmpty(); i++) {

            // **** remove element from the pq ****
            PQNode node = pq.remove();

            // **** add value to the sorted array ****
            output[i] = node.val; 

            // **** add element to the pq (if needed) ****
            if (node.ei < arrs[node.ai].length - 1)
                pq.add(new PQNode(node.ai, node.ei + 1, arrs[node.ai][node.ei + 1]));
        }

        // **** return sorted array ****
        return output;
    }


    /**
     * Test scaffolding
     * 
     * !!!! NOT PART OF SOLUTION !!!!
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // **** initialization ****
        int minSize     = 0;
        int maxSize     = 0;
        int[] output    = null;
        boolean difSize = false;
        int[][] arrs    = null;

        // **** open buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read number of arrays ****
        int n = Integer.parseInt(br.readLine().trim());

        // ???? ????
        System.out.println("main <<<       n: " + n);

        // **** loop reading sorted arrays 
        //      checking if they are of different sizes (if needed) ****
        if (n != 0) {

            // **** declare int[][] array ****
            arrs = new int[n][];

            // **** ****
            minSize = Integer.MAX_VALUE;
            maxSize = Integer.MIN_VALUE;

            // **** read and populate int[][] ****
            for (int i = 0; i < n; i++) {
                arrs[i] = Arrays.stream(br.readLine().trim().split(", "))
                                .mapToInt(Integer::parseInt)
                                .toArray();

                // **** update min size ****
                if (arrs[i].length < minSize)
                    minSize = arrs[i].length;

                // **** update max size ****
                if (arrs[i].length > maxSize)
                    maxSize = arrs[i].length;
            }
        }

        // **** close buffered reader ****
        br.close();

        // **** determine if arrays have different sizes ****
        difSize = (minSize != maxSize);

        // ???? ????
        System.out.println("main <<< minSize: " + minSize);
        System.out.println("main <<< maxSize: " + maxSize);
        System.out.println("main <<< difSize: " + difSize);
        System.out.println("main <<<    arrs: ");
        for (int i = 0; i < n; i++)
            System.out.println(Arrays.toString(arrs[i]));

        // **** arrays must be of the same length ****
        if (!difSize) {

            // **** ****
            output = mergeSameLength(arrs);

            // ???? ????
            if (output != null)
                System.out.println("main <<< mergeSameLength: " + Arrays.toString(output));
        }
    
        // **** simple approach ****
        output = copySort(arrs);

        // ???? ????
        if (output != null)
            System.out.println("main <<<        copySort: " + Arrays.toString(output));

        // **** arrays may be any length ****
        output = merge1(arrs);

        // ???? ????
        if (output != null)
            System.out.println("main <<<          merge1: " + Arrays.toString(output));

        // **** arrays may be any length ****
        output = merge2(arrs);

        // ???? ????
        if (output != null)
            System.out.println("main <<<          merge2: " + Arrays.toString(output));
    }
}