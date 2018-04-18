package com.at.hive;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
/**
 * <p><strong>WARNING:</strong> This should be packed as a generic jar, but not in hadoop jar format</p>
 * <p>Though org.apache.hadoop.hive.ql.exec.UDAF is deprecated, 
 * {@link org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver} and {@link org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2} are too complicated to be used.<p>
 * 
 * <p><strong>Usage:</strong>
 *   <ol>
 *      <li>hadoop fs -copyFromLocal hadoop-0.0.1-SNAPSHOT.jar .</li>
 *      <li>hive> create function mean as 'com.at.hive.Mean' using jar 'hdfs://localhost:9000/user/user399712114676/hadoop-0.0.1-SNAPSHOT.jar' ;</li>
 *      <li>hive> select mean(field1) from some_table ;</li>
 *   </ol>
 * </p>
 * 
 * <p>Notice the difference of using <strong>Double</strong> in the example while it's using <strong>DoubleWritable</strong> in the book.</p>
 */
public class Mean extends UDAF{
    public static class MeanDoubleUDAFEvaluator implements UDAFEvaluator{
        public static class PartialResult{
            double sum;
            long count;
        }
        private PartialResult partial;

        @Override
        public void init() {
            partial = null;
        }
        public boolean iterate(Double value) {
            if(value == null) {
                return true;
            }
            if(partial == null) {
                partial = new PartialResult();
            }
            partial.sum += value;
            partial.count++;
            return true;
        }
        public PartialResult terminatePartial() {
            return partial;
        }
        public boolean merge(PartialResult other) {
            if(other == null) {
                return true;
            }
            if(partial == null) {
                partial = new PartialResult();
            }

            partial.sum += other.sum;
            partial.count += other.count;
            return true;
        }
        public Double terminate() {
            if(partial == null) {
                return null;
            }
            return new Double(partial.sum / partial.count);
        }
    }
}
