package com.at.hadoop.mapreduce;

import java.net.URI;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SortByTemperatureUsingTotalOrderPartitioner extends Configured implements Tool{

    @Override
    public int run(String[] args) throws Exception {
        Job job = JobBuilder.parseInputAndOutput(this, getConf(), args);
        if(job == null) {
            return -1;
        }
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);
        
        // TotalOrderPartitioner
        job.setPartitionerClass(TotalOrderPartitioner.class);
        InputSampler.Sampler<IntWritable, Text> sampler = 
                new InputSampler.RandomSampler<>(
                        0.1 // freq, the frequency of sampling
                        , 10000 // numSamples, max number of samples to obtain from all selected splits.
                        , 10 // maxSplitsSampled, The maximum number of splits to examine (splits of input).
                        );
        InputSampler.writePartitionFile(job, sampler);
        
        // Add to DistrubutedCace
        String partitionFile = 
                TotalOrderPartitioner.getPartitionFile(
                        job.getConfiguration());
        URI partitionUri = new URI(partitionFile);
        job.addCacheFile(partitionUri);
        
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(new SortByTemperatureUsingTotalOrderPartitioner(), args);
        System.exit(exitCode);
    }
}
