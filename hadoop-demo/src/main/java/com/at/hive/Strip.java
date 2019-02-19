package com.at.hive;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * <p><strong>WARNING:</strong> This should be packed as a generic jar, but not in hadoop jar format</p>
 * <ol>
 *  <li>a udf must extend org.apache.hadoop.hive.ql.exec.UDF</li>
 *  <li>a udf must implement at least one evaluate() method</li>
 * </ol>
 * <p><strong>Usage:</strong>
 *   <ol>
 *      <li>hadoop fs -copyFromLocal hadoop-0.0.1-SNAPSHOT.jar .</li>
 *      <li>hive> create function strip as 'com.at.hive.Strip' using jar 'hdfs://localhost:9000/user/user399712114676/hadoop-0.0.1-SNAPSHOT.jar' ;</li>
 *      <li>hive> select strip('    abc      ') from some_table ;</li>
 *   </ol>
 * </p>
 */
public class Strip extends UDF{
    private Text result = new Text();
    
    public Text evaluate(Text str) {
        if(str == null) {
            return null;
        }
        result.set(StringUtils.strip(str.toString()));
        return result;
    }
    
    public Text evaluate(Text str, String stripChars) {
        if(str == null) {
            return null;
        }
        result.set(StringUtils.strip(str.toString(), stripChars));
        return result;
    }
}
