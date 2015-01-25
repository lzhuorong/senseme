package feature;

import data.CsvDataSource;
import data.Record;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by xie on 15-1-23.
 */
public class FeatureExtractionTest {
    @Test
    public void featureExtractionTest(){
        String file="data/dataset1_20150117.csv";
        CsvDataSource csvDataSource=new CsvDataSource();
        List<Record> list=csvDataSource.getRecords(file,2.56,1.28);
        Preprocess preprocess=new Preprocess();
        list=preprocess.extendWorldCordData(list);
        FeatureExtraction featureExtraction=new FeatureExtraction();
        Dataset dataset=featureExtraction.featureExtraction(list);
        String output="data/feature.txt";
        File outputfile=new File(output);
        dataset.serializeToFile(outputfile);
    }
}
