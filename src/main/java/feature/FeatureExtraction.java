package feature;

import data.Constant;
import data.Record;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jtransforms.fft.DoubleFFT_1D;
import util.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public class FeatureExtraction {

    public Dataset featureExtraction(List<Record> recordList){
        Dataset dataset=new Dataset();
        for(Record record:recordList){
            MLData mlData=new MLData();
            double[] timefeatures=extractTimeDomainFeatures(record);
            double[] freqfeatures=extractFreqDomainFeatures(record);
            mlData.add(timefeatures);
            mlData.add(freqfeatures);
            mlData.setLabel(Constant.activityLabelMap.get(record.getType()));
            dataset.addMLData(mlData);
        }
        return dataset;
    }


    public double[] extractTimeDomainFeatures(Record record){
        double[] statistics_features=extractStatisticsFeatures(record);
        double[] correlation_features=extractCorrelationFeatures(record);
        double[] localslope_features=extractLocalSlopeFeatures(record);
        double[] zerocrossrate_features=extractZeroCrossRateFeatures(record);
        int n=statistics_features.length+correlation_features.length+localslope_features.length+zerocrossrate_features.length;
        double[] timeDomainFeatures=new double[n];
        System.arraycopy(statistics_features,0,timeDomainFeatures,0,statistics_features.length);
        System.arraycopy(correlation_features,0,timeDomainFeatures,statistics_features.length,correlation_features.length);
        System.arraycopy(localslope_features,0,timeDomainFeatures,statistics_features.length+correlation_features.length,
                localslope_features.length);
        System.arraycopy(zerocrossrate_features,0,timeDomainFeatures,statistics_features.length+correlation_features.length+localslope_features.length,
                zerocrossrate_features.length);
        return timeDomainFeatures;
    }
 
    public double[] extractFreqDomainFeatures(Record record){
        double[] fft_features=extractFFTFeatures(record);
        int n=fft_features.length;
        double[] freqDomainFeatures=new double[n];
        System.arraycopy(fft_features,0,freqDomainFeatures,0,fft_features.length);
        return freqDomainFeatures;
    }


    public double[] extractStatisticsFeatures(Record record){
        double[] features=new double[Constant.num_attributes*Constant.num_statistics];
        int index=0;
        for(String attribute: Constant.attributeIndexMap.keySet()){
            double[] vector=record.getAttributeVector(attribute);
            double[] feature=getDescriptiveStatistics(vector);
            for(int i=0;i<Constant.num_statistics;i++){
                features[index++]=feature[i];
            }
        }
        return features;
    }

    public double[] extractCorrelationFeatures(Record record){
        double[] res=new double[Constant.num_attributes*Constant.num_correlation];
        int index=0;
        for(int i=0;i<record.getCols();i+=3){
            double[] x=record.getAttributeVector(i);
            double[] y=record.getAttributeVector(i+1);
            double[] z=record.getAttributeVector(i+2);
            double[] xy_correlations=getCorrelations(x, y);
            double[] xz_correlations=getCorrelations(x, z);
            double[] yz_correlations=getCorrelations(y, z);
            for(int j=0;j<Constant.num_correlation;j++){
                res[index++]=xy_correlations[j];
                res[index++]=xz_correlations[j];
                res[index++]=yz_correlations[j];
            }
        }
        return res;
    }

    public double[] extractLocalSlopeFeatures(Record record){
        int num=record.getLength()/Constant.num_regressiondata;
        double[] features=new double[Constant.num_attributes*num];
        int index=0;
        for(int i=0;i<record.getCols();i++){
            double[] vector=record.getAttributeVector(i);
            double[] feature=getLocalSlope(vector);
            System.arraycopy(feature,0,features,i*num,feature.length);
        }
        return features;
    }

    public double[] extractZeroCrossRateFeatures(Record record){
        double[] features=new double[Constant.num_attributes];
        int index=0;
        for(int i=0;i<record.getCols();i++){
            double[] vector=record.getAttributeVector(i);
            double zcr=getZeroCrossRate(vector);
            features[index++]=zcr;
        }
        return features;
    }

    /**
     *
     * @param record
     * @return
     */
    public double[] extractFFTFeatures(Record record){
        double[] features=new double[5*3*10];
        List<double[]> list=new ArrayList<double[]>();
        list.add(record.getAttributeVector("accX",record.getLength()*2));
        list.add(record.getAttributeVector("accY",record.getLength()*2));
        list.add(record.getAttributeVector("accZ",record.getLength()*2));
        list.add(record.getAttributeVector("gyroX",record.getLength()*2));
        list.add(record.getAttributeVector("gyroY",record.getLength()*2));
        list.add(record.getAttributeVector("gyroZ",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccX",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccY",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccZ",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccEarthX",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccEarthY",record.getLength()*2));
        list.add(record.getAttributeVector("linearAccEarthZ",record.getLength()*2));
        list.add(record.getAttributeVector("gyroEarthX",record.getLength()*2));
        list.add(record.getAttributeVector("gyroEarthY",record.getLength()*2));
        list.add(record.getAttributeVector("gyroEarthZ",record.getLength()*2));
        for(int i=0;i<list.size();i++){
            double[] vector=list.get(i);
            double[] feature=getFFTFeatures(vector);
            System.arraycopy(feature,0,features,i*10,feature.length);
        }
        return features;
    }

    public double[] getDescriptiveStatistics(double[] vector){
        DescriptiveStatistics statistics=new DescriptiveStatistics();
        for(int i=0;i<vector.length;i++){
            statistics.addValue(vector[i]);
        }
        double[] res=new double[Constant.num_statistics];
        res[0]=statistics.getMean();
        res[1]=statistics.getStandardDeviation();
        res[2]=statistics.getMax();
        res[3]=statistics.getMin();
        res[4]=statistics.getPercentile(50);
        res[5]=statistics.getSkewness();
        res[6]=statistics.getKurtosis();
        return res;
    }

    public double[] getCorrelations(double[] x,double[] y){
        double[] correlations=new double[Constant.num_correlation];
        correlations[0]=new Covariance().covariance(x,y);
        correlations[1]=new PearsonsCorrelation().correlation(x,y);
        correlations[2]=new SpearmansCorrelation().correlation(x,y);
        correlations[3]=new KendallsCorrelation().correlation(x,y);
        return correlations;
    }

    public double[] getLocalSlope(double[] vector){
        int num=vector.length/Constant.num_regressiondata;
        double[] slopes=new double[num];
        int index=0;
        SimpleRegression regression=new SimpleRegression();
        for(int i=0;i<num;i++){
            regression.clear();
            for(int j=0;j<Constant.num_regressiondata;j++){
                regression.addData(j+1,vector[i*Constant.num_regressiondata+j]);
            }
            slopes[index++]=regression.getSlope();
        }
        return slopes;
    }


    public double getZeroCrossRate(double[] vector){
        int count=0;
        for(int i=1;i<vector.length;i++){
            if(vector[i-1]*vector[i]<0) count++;
        }
        return count*1.0/(vector.length-1);
    }

    public double[] getFFTFeatures(double[] vector){
        int L=vector.length/2;
        DoubleFFT_1D fft_1D=new DoubleFFT_1D(L);
        fft_1D.realForwardFull(vector);
        double T=L*1.0/Constant.freq;
        double[] power=new double[L];
        double[] freq=new double[L];
        for(int i=0;i<L;i++){
            power[i]= Math.sqrt(vector[2*i]*vector[2*i]+vector[2*i+1]*vector[2*i+1])/T;
            freq[i]=i*25*1.0/L;
        }
        Sort.qsort(0,L-1,power,freq,false); //降序
        double[] res=new double[10];
        int index=0;
        for(int i=0;i<5;i++){
            res[index++]=power[i];
            res[index++]=freq[i];
        }
        return res;
    }



    public static void main(String[] args) {
        double[] src={1,2,3};
        double[] des=new double[6];
        System.arraycopy(src,0,des,0,src.length);
        System.arraycopy(src,0,des,3,src.length);
        for(int i=0;i<des.length;i++) System.out.println(des[i]+" ");
    }
}
