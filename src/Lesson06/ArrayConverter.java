package Lesson06;

import java.util.Arrays;

public class ArrayConverter {
    /*public static void main(String[] args) {
        int [] array = {2,5,6,7,4,8,5,6,7,8,9};
        for (int newArray : convert(array)) {
            System.out.print(newArray + ", ") ;
        }
    }*/
    public ArrayConverter(){}

    public Integer[] convert (Integer[] inputArray) throws RuntimeException {
        Integer[] resultArray = null;
        for (int index = inputArray.length - 1; index >= 0; index--){
            if (inputArray[index] == 4) {
                resultArray = Arrays.copyOfRange(inputArray, index + 1 , inputArray.length);
            }
        }
        if (resultArray == null) {
            throw new RuntimeException();
        } else return resultArray;
    }
}
