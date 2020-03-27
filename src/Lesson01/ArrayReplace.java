package Lesson01;

import javax.jnlp.IntegrationService;

public class ArrayReplace {
    static class TArray<T> {
        T[] array;
        TArray(T[] newArray){
            this.array = newArray.clone();
        }

        public void replace (){
            T value = array[0];
            array[0] = array[1];
            array[1] = value;
        }

        public T[] getArray (){
            return array;
        }

        public String toString() {
            String res = "";
            for (T value : array) {
                res += value.toString() + " ";
            }
            return res;
        }
    }
    public static void main(String[] args) {
        Integer[] intArray = {1,2};
        TArray<Integer> arr = new TArray<Integer>(intArray);
        System.out.println(arr.toString());
        arr.replace();
        System.out.println(arr.toString());
        System.out.println();
        String[] strArray = {"First","Second"};
        TArray<String> arr1 = new TArray<String>(strArray);
        System.out.println(arr1.toString());
        arr1.replace();
        System.out.println(arr1.toString());    }
}
