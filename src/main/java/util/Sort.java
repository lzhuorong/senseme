package util;

/**
 * Created by xie on 15-1-21.
 */
public class Sort {

    public static int partition(int lo,int hi,double[] array,double[] affiliate,boolean order){
        double pivot=array[lo];
        double aff_pivot=0;
        if(affiliate!=null) aff_pivot=affiliate[lo];
        int i=lo;
        for(int j=i+1;j<=hi;j++){
            if(compare(array[j],pivot,order)){
                i++;
                double t=array[i];
                array[i]=array[j];
                array[j]=t;
                if(affiliate!=null){
                    t=affiliate[i];
                    affiliate[i]=affiliate[j];
                    affiliate[j]=t;
                }
            }
        }
        array[lo]=array[i];
        if(affiliate!=null) affiliate[lo]=affiliate[i];
        array[i]=pivot;
        if(affiliate!=null) affiliate[i]=aff_pivot;
        return i;
    }

    /**
     *
     * @param a
     * @param b
     * @param order order=true 升序  order=false 降序
     * @return
     */
    private static boolean compare(double a,double b,boolean order){
        if(order){
            return a<=b;
        }else{
            return a>=b;
        }
    }

    public static void qsort(int lo,int hi,double[] array,double[] affiliate,boolean order){
        if(hi<=lo) return;
        int mid=partition(lo,hi,array,affiliate,order);
        qsort(lo,mid-1,array,affiliate,order);
        qsort(mid+1,hi,array,affiliate,order);
    }
}
