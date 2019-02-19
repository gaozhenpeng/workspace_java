package com.at.hadoop.mapreduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.TaskCounter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MalformedTemperatureFields extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if(args.length != 1) {
            JobBuilder.printUsage(this, "<job id>");
            return -1;
        }
        
        String jobID = args[0];
        Cluster cluster = new Cluster(getConf());
        Job job = cluster.getJob(JobID.forName(jobID));
        if(job == null) {
            System.err.printf("No job with ID %s found.\n", jobID);
            return -1;
        }
        
//        if(!job.isComplete()) {
//            System.err.printf("Job %s is not complete.\n", jobID);
//            return -1;
//        }
        
        Counters counters = job.getCounters();
        long malformed = counters.findCounter(MaxTemperatureWithCounters.Temperature.MALFORMED).getValue();
        long total = counters.findCounter(TaskCounter.MAP_INPUT_RECORDS).getValue();
        
        System.out.printf("Records with malformed temperature fields: %.2f%%\n",  100.0 * malformed / total);
        return 0;
    }

    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(new MalformedTemperatureFields(), args);
        System.exit(exitCode);
    }
}
