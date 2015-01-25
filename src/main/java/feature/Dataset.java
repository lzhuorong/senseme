package feature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public class Dataset {
    private List<MLData> dataset;

    public Dataset(){
        dataset=new ArrayList<MLData>();
    }

    public void addMLData(MLData data){
        this.dataset.add(data);
    }

    public boolean serializeToFile(File file){
        try {
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for(int i=0;i<dataset.size();i++){
                MLData mlData=dataset.get(i);
                pw.println(mlData.toString());
            }
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
