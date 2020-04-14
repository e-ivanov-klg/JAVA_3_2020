package Test;

import Lesson06.ArrayConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.jnlp.IntegrationService;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TestArrayConverter {
    private ArrayConverter converter = null;
    private Integer[] testArray;
    private Object[] resultArray;

    public TestArrayConverter (Object[] inputArray, Object[] resultArray) {
        this.testArray = new Integer[inputArray.length];
        this.resultArray = resultArray;
        for (int i = 0; i < inputArray.length; i++){
            this.testArray[i] = (Integer) inputArray[i];
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[][]> data()  {
        return Arrays.asList (new Object[][][] {
            {{1,2,3,4,5,6,7,8}, {5,6,7,8}},
            {{4,34,56,23}, {34,56,23}},
            {{5,6,78,3,2}, null},
            {{56,25,8,9,2}, {14}}
        });
    }

    @Before
    public void init() {
        converter = new ArrayConverter();
    }

    @Test
    public void testConvert (){
    try {
        Assert.assertArrayEquals((Object[]) converter.convert(testArray), resultArray);
        } catch (RuntimeException exc){
            Assert.assertNull(resultArray);
        }
    }
}
