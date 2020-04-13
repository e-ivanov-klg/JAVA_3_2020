package Test;

import Lesson06.ArrayConverter;
import Lesson06.FindNumbersInArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TestFindNumberInArray {
    private FindNumbersInArray finder = null;
    private int[] testArray;
    private  Object testResult;

    public TestFindNumberInArray(Object[] inputArray, Object[] resultArray) {
        testArray = new int[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            testArray[i] = (int) inputArray[i];
        }
        this.testResult = resultArray[0];
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data()  {
        return Arrays.asList (new Object[][][] {
                {{1,2,3,4,5,6,7,8}, {true}},
                {{4,34,56,23}, {true}},
                {{5,6,78,3,2}, {false}}
            });
    }

    @Before
    public void init() {
        finder = new FindNumbersInArray();
    }

    @Test
    public void testConvert (){
        Assert.assertEquals(finder.findNumbersInArray(testArray), testResult);
    }

}
