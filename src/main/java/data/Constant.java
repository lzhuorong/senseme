package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 15-1-18.
 */
public class Constant {
    public static final int freq=25; //数据采样频率
    public static final int num_sensors=3*6; //acceleration,gyro,mag,orient,gravity,linear_acc
    public static final Map<String,Integer> attributeIndexMap=new HashMap<String,Integer>();
    public static final int num_attributes=24;  //轴数据的个数
    public static final int num_statistics=7;   //描述性统计数量
    public static final int num_correlation=4;  //相关系数数量
    public static final int num_regressiondata=8; //每次用于计算回归直线斜率的局部点的个数
    public static final Map<String,Integer> activityLabelMap=new HashMap<String,Integer>();


    static {
        attributeIndexMap.put("accX",0);
        attributeIndexMap.put("accY",1);
        attributeIndexMap.put("accZ",2);
        attributeIndexMap.put("gyroX",3);
        attributeIndexMap.put("gyroY",4);
        attributeIndexMap.put("gyroZ",5);
        attributeIndexMap.put("magX",6);
        attributeIndexMap.put("magY",7);
        attributeIndexMap.put("magZ",8);
        attributeIndexMap.put("orientX",9);
        attributeIndexMap.put("orientY",10);
        attributeIndexMap.put("orientZ",11);
        attributeIndexMap.put("gravityX",12);
        attributeIndexMap.put("gravityY",13);
        attributeIndexMap.put("gravityZ",14);
        attributeIndexMap.put("linearAccX",15);
        attributeIndexMap.put("linearAccY",16);
        attributeIndexMap.put("linearAccZ",17);
        attributeIndexMap.put("linearAccEarthX",18);
        attributeIndexMap.put("linearAccEarthY",19);
        attributeIndexMap.put("linearAccEarthZ",20);
        attributeIndexMap.put("gyroEarthX",21);
        attributeIndexMap.put("gyroEarthY",22);
        attributeIndexMap.put("gyroEarthZ",23);

        activityLabelMap.put("walk",1);
        activityLabelMap.put("run",2);
        activityLabelMap.put("upstairs",3);
        activityLabelMap.put("downstairs",4);
        activityLabelMap.put("standing",5);
        activityLabelMap.put("sitting",6);
        activityLabelMap.put("biking",7);
        activityLabelMap.put("jump",8);
        activityLabelMap.put("uplift",9);
        activityLabelMap.put("downlift",10);
        activityLabelMap.put("driving",11);
    }
}
