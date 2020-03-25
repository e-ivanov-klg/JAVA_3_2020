package Lesson01.FruitBox;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private List<T> boxList = new ArrayList<>();


    Box (T newFruit) {
        boxList.add(newFruit);
    }

    Box(){}

    public void addFruitToBox(T newFruit){
        boxList.add(newFruit);
    }

    public float getBoxWeigth () {
        float sumWeigth = 0;
        for (T fruit : boxList) {
            sumWeigth += fruit.getWeight();
        }
        return sumWeigth;
    }

    public boolean compare (Box compareBox) {
            if (this.getBoxWeigth() == compareBox.getBoxWeigth()) {
                return true;
            }  else return false;
    }

    public List<T> getBoxList (){
        return this.boxList;
    }

    public void addAll (Box<T> sourceBox) throws CloneNotSupportedException {
        if (this.getClass() == sourceBox.getClass()) {
            this.boxList.addAll(sourceBox.getBoxList());
            sourceBox.getBoxList().clear();
            System.out.println("Содержимое коробки пересыпано.");
        } else {
            System.out.println("Коробки с разными фруктами! Пересыпать не получится!");
        }
    }
}
