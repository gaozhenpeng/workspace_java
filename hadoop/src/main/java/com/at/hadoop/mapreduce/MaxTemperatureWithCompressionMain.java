package com.at.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTemperatureWithCompressionMain {
	/**
	 * > hadoop jar target/hadoop-0.0.1-SNAPSHOT.jar com.at.hadoop.mapreduce.MaxTemperatureWithCompressionMain output input
	 * or
	 * > export HADOOP_USER_CLASSPATH_FIRST=true
	 * > export HADOOP_CLASSPATH=target/hadoop-0.0.1-SNAPSHOT.jar
	 * > hadoop com.at.hadoop.mapreduce.MaxTemperatureWithCompressionMain output input
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		if(args.length != 2){
			System.err.println("Usage: MaxTemperatureWithCompressionMain <outputpath> <inputpath>...");
			System.exit(-1);
		}
		Job job = Job.getInstance();
		job.setJarByClass(MaxTemperatureWithCompressionMain.class);
		job.setJobName("Max Temperature with Compression");
		
		FileOutputFormat.setOutputPath(job, new Path(args[0]));
		for(int i = 1; i < args.length ; i++){
			FileInputFormat.addInputPath(job, new Path(args[i]));
		}
		
		job.setMapperClass(MaxTemperatureMapper.class);
		job.setCombinerClass(MaxTemperatureReducer.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}
}
