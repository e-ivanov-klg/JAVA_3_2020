package Lesson01;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

public class ArrayToList<T>{
    private T[] array;
    List<T> list = new ArrayList<T>();

    ArrayToList(T[] array) { this.array = array.clone(); }

    public List<T> arrayToList () {
        for(T value : array) {
            list.add(value);
        }
        return list;
    }

    public static void main(String[] args) {
        ArrayToList<Integer> arr = new ArrayToList<Integer>(new Integer[]{0, 1, 2, 3, 4, 5});
        List<Integer> newList = arr.arrayToList();
        System.out.println(newList.toString());
        System.out.println();

        ArrayToList<String> arr1 = new ArrayToList<String>(new String[]{"Audi", "Toyota", "Lexus"});
        List<String> newList1 = arr1.arrayToList();
        System.out.println(newList1.toString());

    }

}
