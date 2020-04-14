package Lesson06;

import java.util.Arrays;

public class FindNumbersInArray {
    public boolean findNumbersInArray (int[] inArray) {
        if ((Arrays.binarySearch(inArray, 1) != -1) || (Arrays.binarySearch(inArray, 4) != -1)) {
            return true;
        } else return false;
    }
}
