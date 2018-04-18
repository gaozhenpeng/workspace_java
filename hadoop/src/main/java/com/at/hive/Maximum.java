package com.at.hive;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.IntWritable;

/**
 * <p><strong>WARNING:</strong> This should be packed as a generic jar, but not in hadoop jar format</p>
 * <p>Though org.apache.hadoop.hive.ql.exec.UDAF is deprecated, 
 * {@link org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver} and {@link org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2} are too complicated to be used.<p>
 * 
 * <p><strong>Usage:</strong>
 *   <ol>
 *      <li>hadoop fs -copyFromLocal hadoop-0.0.1-SNAPSHOT.jar .</li>
 *      <li>hive> create function maximum as 'com.at.hive.Maximum' using jar 'hdfs://localhost:9000/user/user399712114676/hadoop-0.0.1-SNAPSHOT.jar' ;</li>
 *      <li>hive> select maximum(field1) from some_table ;</li>
 *   </ol>
 * </p>
 */
public class Maximum extends UDAF{
    public static class MaximumIntUDAFEvaluator implements UDAFEvaluator{
        private IntWritable result;
        @Override
        public void init() {
            result = null;
        }
        public boolean iterate(IntWritable value) {
            if(value == null) {
                return true;
            }
            
            if(result == null) {
                result = new IntWritable(value.get());
            }else {
                result.set(Math.max(result.get(), value.get()));
            }
            return true;
        }
        public IntWritable terminatePartial(){
            return result;
        }
        public boolean merge(IntWritable other) {
            return iterate(other);
        }
        public IntWritable terminate() {
            return result;
        }
    }
}
