package mate.academy;

import java.util.concurrent.RecursiveAction;

public class MergeSortAction extends RecursiveAction {

    private static final int MIN_VAL = 2;
    private int[] array;
    private int left;
    private int right;

    public MergeSortAction(int[] array) {
        this.array = array;
        left = 0;
        right = array.length - 1;
    }

    public MergeSortAction(int[] array, int start, int end) {
        this.array = array;
        this.left = start;
        this.right = end;
    }

    @Override
    protected void compute() {
        if (array.length == 0) {
            return;
        }
        if (right - left < MIN_VAL) {
            System.out.printf("%nThread %s sorting elements on position %d and %d.",
                    Thread.currentThread().getName(), left, right);
            if (array[right] < array[left]) {
                int temp = array[right];
                array[right] = array[left];
                array[left] = temp;
            }
        } else {
            int middle = left + (right - left) / 2;
            MergeSortAction leftSort = new MergeSortAction(array, left, middle);
            MergeSortAction rightSort = new MergeSortAction(array, middle + 1, right);

            leftSort.fork();
            rightSort.compute();
            leftSort.join();

            merge(array, left, middle, right);
        }
    }

    private void merge(int[] arr, int l, int m, int r) {
        int sizeFirst = m - l + 1;
        int sizeSecond = r - m;

        int[] leftA = new int[sizeFirst];
        int[] rightA = new int[sizeSecond];

        System.arraycopy(arr, l, leftA, 0, sizeFirst);
        System.arraycopy(arr, m + 1, rightA, 0, sizeSecond);

        int i = 0;
        int j = 0;

        int k = l;
        while (i < sizeFirst && j < sizeSecond) {
            if (leftA[i] <= rightA[j]) {
                arr[k] = leftA[i];
                i++;
            } else {
                arr[k] = rightA[j];
                j++;
            }
            k++;
        }

        while (i < sizeFirst) {
            arr[k] = leftA[i];
            i++;
            k++;
        }

        while (j < sizeSecond) {
            arr[k] = rightA[j];
            j++;
            k++;
        }

    }

}
