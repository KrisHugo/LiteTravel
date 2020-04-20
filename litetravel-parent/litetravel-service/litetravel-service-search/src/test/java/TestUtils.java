import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class TestUtils {
    @Test
    public void getSplits(){
        String money = "1:2";
        String[] split = StringUtils.split(money, ":");
        System.out.println(split.length);
        System.out.println(Arrays.toString(split));

        String money2 = "1:";
        String[] split2 = StringUtils.split(money2, ":");
        System.out.println(split2.length);
        System.out.println(StringUtils.isEmpty(split2[1]));
        System.out.println(Arrays.toString(split2));


        String money3 = ":1";
        String[] split3 = StringUtils.split(money3, ":");
        System.out.println(split3.length);
        System.out.println(StringUtils.isEmpty(split3[0]));
        System.out.println(Arrays.toString(split3));
    }
}
