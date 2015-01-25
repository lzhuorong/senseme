package feature;

/**
 * Created by xie on 15-1-18.
 */
public class MLData {
    private double[] data;
    private int label;
    private int capacity;
    private int current;
    private static final int defaultCapacity=32;

    public MLData(double[] data,int label){
        this.data=data;
        this.label=label;
        this.capacity=data.length;
        this.current=0;
    }

    public MLData(int label){
        this.label=label;
        this.capacity=defaultCapacity;
        data=new double[capacity];
        this.current=0;
    }

    public MLData(){
        this.capacity=defaultCapacity;
        this.current=0;
        data=new double[capacity];
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<current;i++){
            if(i==0){
                builder.append(data[i]);
            }else{
                builder.append(" "+data[i]);
            }
        }
        builder.append(" "+label);
        return builder.toString();
    }

    public void add(double number){
        this.data[current++]=number;
        if(current==capacity){
            resize();
        }
    }

    public void add(double[] numbers){
        for(int i=0;i<numbers.length;i++){
            add(numbers[i]);
        }
    }

    private void resize(){
        double[] temp=data;
        this.capacity=this.capacity*2;
        data=new double[this.capacity];
        System.arraycopy(temp,0,data,0,temp.length);
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
