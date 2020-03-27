package Lesson01.FruitBox;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

public class FruitBoxApp {
    public static void main(String[] args) throws CloneNotSupportedException {
        Box<Apple> appleBox = new Box<Apple>();
        Box<Orange> orangeBox = new Box<Orange>();

        for (int i = 0; i < 4; i++) {
            appleBox.addFruitToBox(new Apple(1.0f));
            orangeBox.addFruitToBox(new Orange(1.5f));
        }

        System.out.println("Вес коробки яблок = " + appleBox.getBoxWeigth());
        System.out.println("Вес коробки апельсинов = " + orangeBox.getBoxWeigth());

        if (appleBox.compare(orangeBox)) {
            System.out.println("Вес коробок равный !");
        } else System.out.println("Вес коробок различный !");

        // Нне компилируется
        //appleBox.addAll(orangeBox);

        Box<Apple> appleBox1 = new Box<Apple>();
        for (int i = 0; i < 2; i++) {
            appleBox1.addFruitToBox(new Apple(1.0f));
        }
        appleBox.addAll(appleBox1);
        System.out.println("Вес 1 коробки яблок = " + appleBox.getBoxWeigth());
        System.out.println("Вес 2 коробки яблок = " + appleBox1.getBoxWeigth());
     }
}
