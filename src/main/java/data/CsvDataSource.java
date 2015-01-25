package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public class CsvDataSource implements DataSource{

    @Override
    public List<Record> getRecords(String fileName,double windowsize,double overlap) {
        int size= (int)(Constant.freq*windowsize);
        int overlapsize=(int) (Constant.freq*overlap);
        List<Record> list=new ArrayList<>();
        Record pre=null;
        Record current=null;
        File file=new File(fileName);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line=br.readLine(); //leave out first line
            while((line=br.readLine())!=null){
                String[] splits=line.split(",");
                double[] sensordata=new double[Constant.num_sensors];
                String type,position,direction,uuid;
                int index=0;
                int start=1;
                int end=18;
                for(int i=start;i<=end;i++){
                    sensordata[index++]=Double.valueOf(splits[i]);
                }
                type=splits[19];
                position=splits[20];
                direction=splits[23];
                uuid=splits[25];
                if(current==null){
                    current=new Record(type,position,direction,uuid,Constant.num_sensors);
                    if(pre!=null){
                        int s=size-overlapsize;
                        int t=s+overlapsize-1;
                        current.copyData(pre,s,t);
                    }
                    current.addRow(sensordata);
                }else if(current.getRowCount()<size){
                    if(current.getUuid().equals(uuid)){
                        current.addRow(sensordata);
                    }else{
                        pre=current;
                        current=new Record(type,position,direction,uuid,Constant.num_sensors);
                        current.addRow(sensordata);
                    }
                }else if(current.getRowCount()==size){
                    list.add(current);
                    pre=current;
                    current=new Record(type,position,direction,uuid,Constant.num_sensors);
                    int s=size-overlapsize;
                    int t=s+overlapsize-1;
                    current.copyData(pre,s,t);
                    current.addRow(sensordata);
                }
            }
            br.close();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String file="data/dataset1_20150117.csv";
        CsvDataSource dataSource=new CsvDataSource();
        List<Record> list=dataSource.getRecords(file,2.56,1.28);
        System.out.println(list.size());
    }
}
