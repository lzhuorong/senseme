package data;

import com.sun.tools.internal.jxc.apt.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public class Record {
    private String type;
    private String position;
    private String uuid;
    private int cols;
    private List<double[]> data;

    public Record(String type,String position,String uuid,int cols){
        this.setType(type);
        this.setPosition(position);
        this.setUuid(uuid);
        this.setCols(cols);
        setData(new ArrayList<double[]>());
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<data.size();i++){
            double[] row=data.get(i);
            for(int j=0;j<row.length;j++){
                builder.append(row[j]+" ");
            }
            builder.append(type+" ");
            builder.append(position+" ");
            builder.append(uuid+"\n");
        }
        return builder.toString();
    }

    public double[] getAttributeVector(String attribute){
        int index=Constant.attributeIndexMap.get(attribute);
        return getAttributeVector(index);
    }

    public double[] getAttributeVector(int index){
        if(index<0||index>cols) return null;
        double[] vector=new double[data.size()];
        for(int i=0;i<data.size();i++){
            vector[i]=data.get(i)[index];
        }
        return vector;
    }

    public double[] getAttributeVector(String attribute,int capacity){
        int index= Constant.attributeIndexMap.get(attribute);
        return getAttributeVector(index,capacity);
    }

    public double[] getAttributeVector(int index,int capacity){
        if(index<0||index>cols) return null;
        int size=capacity>data.size()? capacity:data.size();
        double[] vector=new double[size];
        for(int i=0;i<data.size();i++){
            vector[i]=data.get(i)[index];
        }
        return vector;
    }



    public boolean addRow(double[] row){
        if(row.length!= getCols()) return false;
        getData().add(row);
        return true;
    }

    public int getRowCount(){
        return getData().size();
    }

    public void copyData(Record another,int start,int end){
        if(another==null||!isSame(another)) return;
        end=end< another.getData().size()? end: another.getData().size();
        for(int i=start;i<=end;i++){
            double[] row= another.getData().get(i);
            double[] copyrow=new double[row.length];
            System.arraycopy(row,0,copyrow,0,row.length);
            addRow(copyrow);
        }
    }

    public int getLength(){
        return data.size();
    }

    public boolean isSame(Record another){
        return this.getUuid().equals(another.getUuid());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public List<double[]> getData() {
        return data;
    }

    public void setData(List<double[]> data) {
        this.data = data;
    }
}
