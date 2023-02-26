package first_version;

import java.util.Arrays;

public record Command(String command, String[] args) {
    public static void main(String[] args) {
        String[] arr = new String[3];
        arr[0] = "1";
        arr[1] = "2";
        arr[2] = "3";
        for (var el : arr) {
            el = "a";
        }
        System.out.println(Arrays.toString(arr));
    }
}


