package data;

import java.util.List;

/**
 * Created by xie on 15-1-18.
 */
public interface DataSource {
    List<Record> getRecords(String fileName,double windowsize,double overlap);
}
