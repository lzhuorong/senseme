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
        String[] input={
                "data/dataset1_trousers_verticaloutside_20150117.csv",
                "data/dataset2_trousers_landinside_20150125.csv"
        };
        String output="data/feature.txt";
        double windowsize=2.56;
        double overlapsize=1.28;
        FeatureExtraction featureExtraction=new FeatureExtraction();
        Dataset dataset=featureExtraction.featureExtraction(windowsize,overlapsize,input);
        File file=new File(output);
        dataset.serializeToFile(file);
    }
}
