package com.at.hbase;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.at.hadoop.mapreduce.JobBuilder;

/**
 * mvn clean package
 * set HBASE_CLASSPATH=target\hadoop-0.0.1-SNAPSHOT.jar
 * 
 * %HBASE_HOME%\bin\hbase.cmd com.at.hbase.HBaseRowCounter test
 */
public class HBaseRowCounter extends Configured implements Tool {
    static class RowCounterMapper extends TableMapper<ImmutableBytesWritable, Result>{
        /** user-defined counter */
        public static enum Counters { ROWS }
        
        @Override
        public void map(ImmutableBytesWritable row, Result value, Context context) {
            context.getCounter(Counters.ROWS).increment(1);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Job job = JobBuilder.parseArgs(this, getConf(), args);
        
        String tableName  = args[0];
        Scan scan = new Scan();
        scan.setFilter(new FirstKeyOnlyFilter());
        
        TableMapReduceUtil.initTableMapperJob(
                tableName
                , scan
                , RowCounterMapper.class
                , ImmutableBytesWritable.class
                , Result.class
                , job);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(NullOutputFormat.class);
        
        return job.waitForCompletion(true) ? 0 : 1;
    }
    
    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(HBaseConfiguration.create(), new HBaseRowCounter(), args);
        
        System.exit(exitCode);
    }
}
