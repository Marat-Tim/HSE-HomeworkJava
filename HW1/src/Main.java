public class Main {
    public static void bubbleSort(int[] array) {
        boolean isSorted = false;
        int i = array.length - 1;
        while (!isSorted) {
            isSorted = true;
            for (int j = 0; j < i; j++) {
                if (array[j] > array[j + 1]) {
                    array[j] ^= array[j + 1];
                    array[j + 1] ^= array[j];
                    array[j] ^= array[j + 1];
                    isSorted = false;
                }
            }
            i--;
        }
    }

    public static void printlnArray(int[] array) {
        for (int element: array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] array = { 5, 4, 2, 3, 1 };
        bubbleSort(array);
        printlnArray(array);
    }
}