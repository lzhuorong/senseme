package util;

import org.junit.Test;

/**
 * Created by xie on 15-1-21.
 */
public class SortTest {
    @Test
    public void testQsort(){
        double[] data=new double[]{1.2,4,3,2,8.9};
        double[] aff=new double[]{1.2,4,3,2,8.9};
        Sort.qsort(0,data.length-1,data,aff,true);
        for(int i=0;i<data.length;i++){
            System.out.println(data[i]+" "+aff[i]);
        }
    }
}
