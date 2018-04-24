package com.at.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

public class JobBuilder {
    public static Job parseInputAndOutput(Tool tool, Configuration conf, String[] args) throws IOException {
        if(args.length < 2) {
            printUsage(tool, "<output> <input1> [...<inputN>]");
            return null;
        }
        Job job = Job.getInstance(conf);
        job.setJarByClass(tool.getClass());
        job.setJobName(tool.getClass().getSimpleName());
        
        FileOutputFormat.setOutputPath(job, new Path(args[0]));
        for(int i = 1; i < args.length ; i++){
            FileInputFormat.addInputPath(job, new Path(args[i]));
        }
        
        
        return job;
    }
    public static Job parseArgs(Tool tool, Configuration conf, String[] args) throws IOException {
        if(args.length < 1) {
            printUsage(tool, "<arg1> [...<argN>]");
            return null;
        }
        Job job = Job.getInstance(conf);
        job.setJarByClass(tool.getClass());
        job.setJobName(tool.getClass().getSimpleName());
        
        return job;
    }

    public static void printUsage(Tool tool, String extraArgsUsage) {
        System.err.printf("Usage: %s [genericOptions] %s\n\n", tool.getClass().getSimpleName(), extraArgsUsage);
        GenericOptionsParser.printGenericCommandUsage(System.err);
    }

}
