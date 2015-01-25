package feature;

import data.CsvDataSource;
import data.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public class Preprocess {

    public List<Record> extendWorldCordData(List<Record> list){
        List<Record> recordList=new ArrayList<Record>();
        for(Record record:list){
            String type=record.getType();
            String position=record.getPosition();
            String direction=record.getDirection();
            String uuid=record.getUuid();
            Record rec=new Record(type,position,direction,uuid,record.getCols()+6);
            List<double[]> data=record.getData();
            for(int i=0;i<data.size();i++){
                double[] row=data.get(i);
                double[] nrow=new double[rec.getCols()];
                System.arraycopy(row,0,nrow,0,row.length);
                float[] gravity=new float[3];
                float[] mag=new float[3];
                float[] acc=new float[3];
                float[] lg=new float[3];
                float[] gryo=new float[3];
                float[] rotation=new float[9];
                float[] incline=new float[9];
                acc[0]= (float) row[0];
                acc[1]= (float) row[1];
                acc[2]= (float) row[2];
                gryo[0]= (float) row[3];
                gryo[1]= (float) row[4];
                gryo[2]= (float) row[5];
                gravity[0]= (float) row[12];
                gravity[1]= (float) row[13];
                gravity[2]= (float) row[14];
                mag[0]= (float) row[6];
                mag[1]= (float) row[7];
                mag[2]= (float) row[8];
                lg[0]=acc[0]-gravity[0];
                lg[1]=acc[1]-gravity[1];
                lg[2]=acc[2]-gravity[2];
                getRotationMatrix(rotation,incline,gravity,mag);
                float[] linearAccEarth=matrixMultiply(rotation,lg);
                float[] gyroEarth=matrixMultiply(rotation,gryo);
                int n=record.getCols();
                nrow[n]=linearAccEarth[0];
                nrow[n+1]=linearAccEarth[1];
                nrow[n+2]=linearAccEarth[2];
                nrow[n+3]=gyroEarth[0];
                nrow[n+4]=gyroEarth[1];
                nrow[n+5]=gyroEarth[2];
                rec.addRow(nrow);
            }
            recordList.add(rec);
        }
        return recordList;
    }

    public boolean serializeToFile(File file,List<Record> list){
        try {
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for(int i=0;i<list.size();i++){
                Record record=list.get(i);
                pw.print(record.toString());
            }
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public float[] matrixMultiply(float[] matrix,float[] vector){
        float[] res=new float[vector.length];
        int rows=matrix.length/vector.length;
        int cols=vector.length;
        for(int i=0;i<rows;i++){
            float sum=0;
            for(int j=0;j<cols;j++){
                sum+=matrix[i*cols+j]*vector[j];
            }
            res[i]=sum;
        }
        return res;
    }

    /**
     * copy from android source code.
     * rotation matrix: transfer a vector from device coordinate system to the world coordinate system
     * @param R
     * @param I
     * @param gravity
     * @param geomagnetic
     * @return
     */
    public boolean getRotationMatrix(float[] R, float[] I,
                                            float[] gravity, float[] geomagnetic) {
        // TODO: move this to native code for efficiency
        float Ax = gravity[0];
        float Ay = gravity[1];
        float Az = gravity[2];
        final float Ex = geomagnetic[0];
        final float Ey = geomagnetic[1];
        final float Ez = geomagnetic[2];
        float Hx = Ey*Az - Ez*Ay;
        float Hy = Ez*Ax - Ex*Az;
        float Hz = Ex*Ay - Ey*Ax;
        final float normH = (float)Math.sqrt(Hx*Hx + Hy*Hy + Hz*Hz);
        if (normH < 0.1f) {
            // device is close to free fall (or in space?), or close to
            // magnetic north pole. Typical values are  > 100.
            return false;
        }
        final float invH = 1.0f / normH;
        Hx *= invH;
        Hy *= invH;
        Hz *= invH;
        final float invA = 1.0f / (float)Math.sqrt(Ax*Ax + Ay*Ay + Az*Az);
        Ax *= invA;
        Ay *= invA;
        Az *= invA;
        final float Mx = Ay*Hz - Az*Hy;
        final float My = Az*Hx - Ax*Hz;
        final float Mz = Ax*Hy - Ay*Hx;
        if (R != null) {
            if (R.length == 9) {
                R[0] = Hx;     R[1] = Hy;     R[2] = Hz;
                R[3] = Mx;     R[4] = My;     R[5] = Mz;
                R[6] = Ax;     R[7] = Ay;     R[8] = Az;
            } else if (R.length == 16) {
                R[0]  = Hx;    R[1]  = Hy;    R[2]  = Hz;   R[3]  = 0;
                R[4]  = Mx;    R[5]  = My;    R[6]  = Mz;   R[7]  = 0;
                R[8]  = Ax;    R[9]  = Ay;    R[10] = Az;   R[11] = 0;
                R[12] = 0;     R[13] = 0;     R[14] = 0;    R[15] = 1;
            }
        }
        if (I != null) {
            // compute the inclination matrix by projecting the geomagnetic
            // vector onto the Z (gravity) and X (horizontal component
            // of geomagnetic vector) axes.
            final float invE = 1.0f / (float)Math.sqrt(Ex*Ex + Ey*Ey + Ez*Ez);
            final float c = (Ex*Mx + Ey*My + Ez*Mz) * invE;
            final float s = (Ex*Ax + Ey*Ay + Ez*Az) * invE;
            if (I.length == 9) {
                I[0] = 1;     I[1] = 0;     I[2] = 0;
                I[3] = 0;     I[4] = c;     I[5] = s;
                I[6] = 0;     I[7] =-s;     I[8] = c;
            } else if (I.length == 16) {
                I[0] = 1;     I[1] = 0;     I[2] = 0;
                I[4] = 0;     I[5] = c;     I[6] = s;
                I[8] = 0;     I[9] =-s;     I[10]= c;
                I[3] = I[7] = I[11] = I[12] = I[13] = I[14] = 0;
                I[15] = 1;
            }
        }
        return true;
    }



    public static void main(String[] args) {
        String file="data/dataset1_20150117.csv";
        CsvDataSource csvDataSource=new CsvDataSource();
        List<Record> list=csvDataSource.getRecords(file,2.56,1.28);
        Preprocess preprocess=new Preprocess();
        list=preprocess.extendWorldCordData(list);
    }
}
